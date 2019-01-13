package br.com.tsmweb.carros.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.adapter.CarroAdapter;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.CarroDB;
import br.com.tsmweb.carros.domain.CarroService;
import br.com.tsmweb.carros.utils.IOUtils;
import br.com.tsmweb.carros.utils.SDCardUtils;

public class CarrosViewModal extends AndroidViewModel implements LifecycleObserver {

    private static final String TAG = CarroViewModal.class.getSimpleName();

    private CarroAdapter adapter = new CarroAdapter(this);
    private MutableLiveData<Carro> selected;
    private MutableLiveData<Carro> selectedLong = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleted;
    private MutableLiveData<ArrayList<Uri>> imageUris;
    private MutableLiveData<Boolean> downloadingImage = new MutableLiveData<>();
    private boolean pullToRefresh;
    private int countSelectedCarro = 0;

    public CarrosViewModal(@NonNull Application application) {
        super(application);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void reset() {
        selected = new MutableLiveData<>();
        deleted = new MutableLiveData<>();
        imageUris = new MutableLiveData<>();
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

    public boolean getPullToRefresh() {
        return pullToRefresh;
    }

    public void setPullToRefresh(boolean pullToRefresh) {
        this.pullToRefresh = pullToRefresh;
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
       new GetCarrosTask(pullToRefresh).execute(tipo);
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
        CarroDB db = new CarroDB(getApplication().getApplicationContext());

        try {
            for (Carro c : getSelectedCarros()) {
                db.delete(c);
                adapter.getCarros().remove(c);
                countSelectedCarro--;
            }
        } finally {
            db.close();
        }

        deleted.postValue(true);
    }

    // Compartilha os carros selecionados
    public void shareSelectedCarros() {
        new ShareCarrosTask().execute(getSelectedCarros());
    }

    // Task para buscar os carros
    private class GetCarrosTask extends AsyncTask<Integer, Void, List<Carro>> {

        private boolean refresh;

        public GetCarrosTask(boolean refresh) {
            this.refresh = refresh;
        }

        @Override
        protected List<Carro> doInBackground(Integer... params) {
            loading.postValue(true);

            try {
                //Thread.sleep(3000);
                // Busca os carros em background (Thread)
                return CarroService.getCarros(getApplication().getApplicationContext(), params[0], refresh);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Carro> carros) {
            if (carros != null) {
                adapter.setCarros(carros);
            }

            loading.postValue(false);
        }
    }

    // Task para fazer o download
    private class ShareCarrosTask extends AsyncTask<List<Carro>, Void, ArrayList<Uri>> {

        @Override
        protected void onPreExecute() {
            downloadingImage.postValue(true);
        }

        @Override
        protected ArrayList<Uri> doInBackground(List<Carro>... selectedCarros) {
            ArrayList<Uri> listUri = new ArrayList<>();

            if (selectedCarros != null && selectedCarros.length > 0) {
                for (Carro c : selectedCarros[0]) {
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
        }

        @Override
        protected void onPostExecute(ArrayList<Uri> uris) {
            if (uris != null && uris.size() > 0) {
                imageUris.postValue(uris);
            }

            downloadingImage.postValue(false);
        }
    }

}
