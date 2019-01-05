package br.com.tsmweb.carros.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.adapter.CarroAdapter;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.CarroDB;
import br.com.tsmweb.carros.domain.CarroService;

public class CarrosViewModal extends AndroidViewModel implements LifecycleObserver {

    private static final String TAG = CarroViewModal.class.getName();

    private CarroAdapter adapter = new CarroAdapter(this);
    private MutableLiveData<Carro> selected;
    private MutableLiveData<Carro> selectedLong = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleted;
    private boolean pullToRefresh;
    private int countSelectedCarro = 0;

    public CarrosViewModal(@NonNull Application application) {
        super(application);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void reset() {
        selected = new MutableLiveData<>();
        deleted = new MutableLiveData<>();
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
        carro.selected = !carro.selected;

        if (carro.selected) {
            countSelectedCarro++;
        }  else {
            countSelectedCarro--;
        }

        selected.postValue(carro);
    }

    public boolean onCarroItemLongClick(Carro carro) {
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

        for (Carro c : adapter.getCarros()) {
            if (c.selected) {
                list.add(c);
            }
        }

        return list;
    }

    // Configura todos os carros para não selecionados
    public void deselectCarros() {
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

}
