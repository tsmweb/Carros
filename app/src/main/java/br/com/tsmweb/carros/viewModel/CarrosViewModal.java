package br.com.tsmweb.carros.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import br.com.tsmweb.carros.adapter.CarroAdapter;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.CarroService;

public class CarrosViewModal extends AndroidViewModel {

    private static final String TAG = CarroViewModal.class.getName();

    private CarroAdapter adapter;
    private MutableLiveData<Carro> selected;
    private MutableLiveData<Boolean> loading;
    private boolean pullToRefresh;

    public CarrosViewModal(@NonNull Application application) {
        super(application);
    }

    public void init() {
        adapter = new CarroAdapter(this);
        selected = new MutableLiveData<>();
        loading = new MutableLiveData<>();
    }

    public CarroAdapter getAdapter() {
        return adapter;
    }

    public LiveData<Carro> getSelected() {
        return selected;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public boolean getPullToRefresh() {
        return pullToRefresh;
    }

    public void setPullToRefresh(boolean pullToRefresh) {
        this.pullToRefresh = pullToRefresh;
    }

    public void onCarroItemClick(Carro carro) {
        selected.postValue(carro);
    }

    public void loadCarros(int tipo) {
       new GetCarrosTask(pullToRefresh).execute(tipo);
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
                Thread.sleep(3000);
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
