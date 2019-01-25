package br.com.tsmweb.carros.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.adapter.CarroAdapter;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.repository.ICarroRepository;
import br.com.tsmweb.carros.domain.repository.RepositoryLocator;
import br.com.tsmweb.carros.utils.IOUtils;
import br.com.tsmweb.carros.utils.SDCardUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CarrosViewModal extends AndroidViewModel implements LifecycleObserver {

    private static final String TAG = CarroViewModal.class.getSimpleName();

    private ICarroRepository carroRepository;

    private CarroAdapter adapter = new CarroAdapter(this);
    private MutableLiveData<Carro> selected;
    private MutableLiveData<Carro> selectedLong = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleted;
    private MutableLiveData<ArrayList<Uri>> imageUris;
    private MutableLiveData<Boolean> downloadingImage = new MutableLiveData<>();
    private MutableLiveData<Boolean> error;
    private MutableLiveData<Boolean> pullToRefresh = new MutableLiveData<>();
    private int countSelectedCarro = 0;

    public CarrosViewModal(@NonNull Application application) {
        super(application);

        try {
            carroRepository = RepositoryLocator.getInstance(getApplication().getApplicationContext()).locate(ICarroRepository.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void reset() {
        selected = new MutableLiveData<>();
        deleted = new MutableLiveData<>();
        imageUris = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public CarroAdapter getAdapter() {
        return adapter;
    }

    public LiveData<Carro> getSelected() {
        return selected;
    }

    public LiveData<Carro> getSelectedLong() {
        return selectedLong;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getDeleted() {
        return deleted;
    }

    public LiveData<ArrayList<Uri>> getImageUris() {
        return imageUris;
    }

    public LiveData<Boolean> getDownloadingImage() {
        return downloadingImage;
    }

    public LiveData<Boolean> getError() {
        return error;
    }

    public LiveData<Boolean> getPullToRefresh() {
        return pullToRefresh;
    }

    public int getCountSelectedCarro() {
        return countSelectedCarro;
    }

    public void onCarroItemClick(Carro carro) {
        if (countSelectedCarro > 0) {
            carro.selected = !carro.selected;

            if (carro.selected) {
                countSelectedCarro++;
            }  else {
                countSelectedCarro--;
            }
        }

        selected.postValue(carro);
    }

    public boolean onCarroItemLongClick(Carro carro) {
        deselectCarros();
        carro.selected = !carro.selected;

        if (carro.selected) {
            countSelectedCarro++;
        }  else {
            countSelectedCarro--;
        }

        selectedLong.postValue(carro);

        return true;
    }

    public void loadCarros(int tipo) {
        loading.postValue(true);

        Disposable d = carroRepository.getCarrosByTipo(tipo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        carros -> {
                            if (!carros.isEmpty()) {
                                adapter.setCarros(carros);
                            }

                            loading.postValue(false);
                        },
                        err -> {
                            loading.postValue(false);
                            error.postValue(true);
                            Log.d(TAG, err.getMessage(), err);
                        });
    }

    public void downloadCarros(int tipo) {
        pullToRefresh.postValue(true);

        Disposable d = carroRepository.downloadCarrosFromAPI(tipo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            pullToRefresh.postValue(false);
                            Log.d(TAG, "Download dos carros do Web Services!");
                        },
                        err -> {
                            pullToRefresh.postValue(false);
                            error.postValue(true);
                            Log.d(TAG, err.getMessage(), err);
                        });
    }

    // Retorna a lista de carros selecionados
    public List<Carro> getSelectedCarros() {
        List<Carro> list = new ArrayList<>();

        if (countSelectedCarro > 0) {
            for (Carro c : adapter.getCarros()) {
                if (c.selected) {
                    list.add(c);
                }
            }
        }

        return list;
    }

    // Configura todos os carros para não selecionados
    public void deselectCarros() {
        if (countSelectedCarro == 0) { return; }

        for (Carro c : adapter.getCarros()) {
            c.selected = false;
        }

        countSelectedCarro = 0;
    }

    // Deleta os carros selecionados
    public void deleteSelectedCarros() {
        List<Carro> selectedCarros = getSelectedCarros();

        Disposable d = carroRepository.delete(selectedCarros)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ret -> {
                            adapter.getCarros().removeAll(selectedCarros);
                            countSelectedCarro -= selectedCarros.size();
                        },
                        err -> {
                            error.postValue(true);
                            Log.d(TAG, err.getMessage(), err);
                        });
    }

    // Compartilha os carros selecionados
    public void shareSelectedCarros() {
        List<Carro> selectedCarros = getSelectedCarros();

        Observable.fromCallable(
                () -> {
                    ArrayList<Uri> listUri = new ArrayList<>();

                    if (selectedCarros != null && selectedCarros.size() > 0) {
                        for (Carro c : selectedCarros) {
                            // Faz download da foto do carro para arquivo
                            String url = c.getUrlFoto();
                            String fileName = url.substring(url.lastIndexOf("/")+1);

                            // Cria o arquivo no SD Card
                            File file = SDCardUtils.getPrivateFile(getApplication().getApplicationContext(), fileName, Environment.DIRECTORY_PICTURES);
                            IOUtils.downloadToFile(c.getUrlFoto(), file);

                            // Salva a Uri para compartilhar a foto
                            listUri.add(Uri.fromFile(file));
                        }
                    }

                    return listUri;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        uris -> {
                            if (uris != null && uris.size() > 0) {
                                imageUris.postValue(uris);
                            }
                        },
                        err -> {
                            error.postValue(true);
                            Log.d(TAG, err.getMessage(), err);
                        },
                        () -> downloadingImage.postValue(false),
                        disposable -> downloadingImage.postValue(true)
                );
    }

}
