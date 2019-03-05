package br.com.tsmweb.carros.presentation.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.data.repository.CarroRepositoryImpl;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import br.com.tsmweb.carros.view.adapter.CarroAdapter;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.utils.IOUtils;
import br.com.tsmweb.carros.utils.SDCardUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CarrosViewModel extends AndroidViewModel implements LifecycleObserver {

    private static final String TAG = CarroViewModel.class.getSimpleName();

    private CarroRepository carroRepository;

    private CarroAdapter adapter = new CarroAdapter(this);
    private MutableLiveData<Carro> selected;
    private MutableLiveData<Carro> selectedLong = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Uri>> imageUris;
    private MutableLiveData<Boolean> downloadingImage = new MutableLiveData<>();
    private MutableLiveData<Boolean> pullToRefresh = new MutableLiveData<>();

    private MutableLiveData<ViewState<Boolean>> viewState = new MutableLiveData<>();
    private int countSelectedCarro = 0;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public CarrosViewModel(@NonNull Application application) {
        super(application);

        // Obtém uma instância de CarroRepository para manipular os dados dos carros
        this.carroRepository = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
        selected = new MutableLiveData<>();
        viewState.postValue(ViewState.delete(false));
        imageUris = new MutableLiveData<>();
        viewState.postValue(ViewState.error(false));
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

    public LiveData<ArrayList<Uri>> getImageUris() {
        return imageUris;
    }

    public LiveData<Boolean> getDownloadingImage() {
        return downloadingImage;
    }

    public LiveData<Boolean> getPullToRefresh() {
        return pullToRefresh;
    }

    public LiveData<ViewState<Boolean>> getState() {
        return viewState;
    }

    public int getCountSelectedCarro() {
        return countSelectedCarro;
    }

    // Seleciona um carro com o evento de click
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

    // Seleciona um carro com o evento de click longo
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

    // Carrega os carros da base de dados
    public void loadCarros(int tipo) {
        //loading.postValue(true);
        viewState.postValue(ViewState.loading(true));

        mCompositeDisposable.add(carroRepository.getCarrosByTipo(tipo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        carros -> {
                            if (!carros.isEmpty()) {
                                adapter.setCarros(carros);
                            }

                            viewState.postValue(ViewState.loading(false));
                        },
                        err -> {
                            viewState.postValue(ViewState.loading(false));
                            viewState.postValue(ViewState.error(err, true));
                            Log.d(TAG, err.getMessage(), err);
                        })
        );
    }

    // Obtém os carros de um web-services e armazena na base de dados
    public void downloadCarros(int tipo) {
        pullToRefresh.postValue(true);

        /*
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(carroRepository.downloadCarrosFromAPI(tipo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> compositeDisposable.clear())
                .subscribe(
                        () -> {
                            pullToRefresh.postValue(false);
                            Log.d(TAG, "Download-Carros : Download dos carros do Web Services!");
                        },
                        err -> {
                            pullToRefresh.postValue(false);
                            viewState.postValue(ViewState.error(err, true));
                            Log.d(TAG, err.getMessage(), err);
                        })
        );
        */
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

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(carroRepository.delete(selectedCarros)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    selectedLong.postValue(null);
                    compositeDisposable.clear();
                })
                .subscribe(
                        ret -> {
                            adapter.getCarros().removeAll(selectedCarros);
                            countSelectedCarro -= selectedCarros.size();
                        },
                        err -> {
                            viewState.postValue(ViewState.error(err, true));
                            Log.d(TAG, err.getMessage(), err);
                        })
        );
    }

    // Compartilha os carros selecionados
    public void shareSelectedCarros() {
        List<Carro> selectedCarros = getSelectedCarros();

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Observable.fromCallable(
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
                .doAfterTerminate(() -> compositeDisposable.clear())
                .subscribe(
                        uris -> {
                            if (uris != null && uris.size() > 0) {
                                imageUris.postValue(uris);
                            }
                        },
                        err -> {
                            downloadingImage.postValue(false);
                            viewState.postValue(ViewState.error(err, true));
                            Log.d(TAG, err.getMessage(), err);
                        },
                        () -> downloadingImage.postValue(false),
                        subscriber -> downloadingImage.postValue(true)
                )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        Log.d(TAG, "onCleared()");
        mCompositeDisposable.clear();
    }
}
