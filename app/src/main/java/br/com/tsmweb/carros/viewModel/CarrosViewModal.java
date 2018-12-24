package br.com.tsmweb.carros.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import br.com.tsmweb.carros.adapter.CarroAdapter;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.CarroService;

public class CarrosViewModal extends AndroidViewModel {

    private CarroAdapter adapter;
    private MutableLiveData<Carro> selected;

    public CarrosViewModal(@NonNull Application application) {
        super(application);
    }

    public void init() {
        adapter = new CarroAdapter(this);
        selected = new MutableLiveData<>();
    }

    public CarroAdapter getAdapter() {
        return adapter;
    }

    public LiveData<Carro> getSelected() {
        return selected;
    }

    public void onCarroItemClick(Carro carro) {
        selected.postValue(carro);
    }

    public void loadCarros(int tipo) {
        List<Carro> carros = CarroService.getCarros(getApplication().getApplicationContext(), tipo);
        adapter.setCarros(carros);
    }

}
