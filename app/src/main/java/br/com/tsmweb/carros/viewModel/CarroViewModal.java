package br.com.tsmweb.carros.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.CarroDB;

public class CarroViewModal extends AndroidViewModel {

    private static final String TAG = CarroViewModal.class.getSimpleName();

    public ObservableField<String> nome = new ObservableField<>();
    public ObservableField<String> descricao = new ObservableField<>();
    public ObservableField<String> urlFoto = new ObservableField<>();

    private MutableLiveData<Boolean> updated = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleted = new MutableLiveData<>();
    private MutableLiveData<Boolean> error = new MutableLiveData<>();

    private Carro carro;

    public CarroViewModal(@NonNull Application application) {
        super(application);
    }

    public void setCarro(Carro carro) {
        this.carro = carro;

        nome.set(carro.getNome());
        descricao.set(carro.getDesc());
        urlFoto.set(carro.getUrlFoto());
    }

    public LiveData<Boolean> getUpdated() {
        return updated;
    }

    public LiveData<Boolean> getDeleted() {
        return deleted;
    }

    public LiveData<Boolean> getError() {
        return error;
    }

    public void onCarroUpdate() {
        // Valida se os dados do carro são válidos
        if (!validateCar()) {
            error.postValue(true);
            return;
        }

        carro.setNome(nome.get());

        // Salva as alterações no banco de dados
        CarroDB db = new CarroDB(getApplication().getApplicationContext());

        try {
            db.save(carro);
        } finally {
            db.close();
        }

        updated.postValue(true);
    }

    // Valida os valores informados pelo usuário
    private boolean validateCar() {
        if (nome.get().trim().length() == 0) {
            return false;
        }

        return true;
    }

    public void onCarroDelete() {
        // Deleta o carro
        CarroDB db = new CarroDB(getApplication().getApplicationContext());

        try {
            db.delete(carro);
        } finally {
            db.close();
        }

        deleted.postValue(true);
    }

}
