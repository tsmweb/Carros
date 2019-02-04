package br.com.tsmweb.carros.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.repository.ICarroRepository;
import br.com.tsmweb.carros.domain.repository.RepositoryLocator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CarroViewModal extends AndroidViewModel {

    private static final String TAG = CarroViewModal.class.getSimpleName();

    private ICarroRepository carroRepository;

    public ObservableField<String> nome = new ObservableField<>();
    public ObservableField<String> descricao = new ObservableField<>();
    public ObservableField<String> urlFoto = new ObservableField<>();

    private MutableLiveData<Boolean> updated = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleted = new MutableLiveData<>();
    private MutableLiveData<Boolean> error = new MutableLiveData<>();

    private Carro carro;

    public CarroViewModal(@NonNull Application application) {
        super(application);

        try {
            carroRepository = RepositoryLocator.getInstance(getApplication().getApplicationContext()).locate(ICarroRepository.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(carroRepository.update(carro)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> compositeDisposable.clear())
                .subscribe(
                    ret -> {
                        updated.postValue(true);
                    },
                    err -> {
                        error.postValue(true);
                    }
                )
        );
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
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(carroRepository.delete(carro)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> compositeDisposable.clear())
                .subscribe(
                    ret -> {
                        deleted.postValue(true);
                    },
                    err ->  {
                        error.postValue(true);
                    }
                )
        );
    }

}
