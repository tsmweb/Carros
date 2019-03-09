package br.com.tsmweb.carros.presentation.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarro;
import br.com.tsmweb.carros.domain.interactor.carros.SaveCarro;
import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.observers.DisposableSingleObserver;

public class CarroViewModel extends AndroidViewModel {

    private static final String TAG = CarroViewModel.class.getSimpleName();

    private final SaveCarro saveCarro;
    private final DeleteCarro deleteCarro;

    public ObservableField<String> nome = new ObservableField<>();
    public ObservableField<String> descricao = new ObservableField<>();
    public ObservableField<String> urlFoto = new ObservableField<>();

    private MutableLiveData<ViewState<Boolean>> viewState = new MutableLiveData<>();

    private Carro carro;

    public CarroViewModel(@NonNull Application application, @NonNull SaveCarro saveCarro, @NonNull DeleteCarro deleteCarro) {
        super(application);

        this.saveCarro = saveCarro;
        this.deleteCarro = deleteCarro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;

        nome.set(carro.getNome());
        descricao.set(carro.getDesc());
        urlFoto.set(carro.getUrlFoto());
    }

    public LiveData<ViewState<Boolean>> getState() {
        return viewState;
    }

    public void onCarroUpdate() {
        // Valida se os dados do carro são válidos
        if (!validateCar()) {
            viewState.postValue(ViewState.error(new IllegalArgumentException(),true));
            return;
        }

        carro.setNome(nome.get());

        // Salva as alterações no banco de dados
        saveCarro.execute(new DisposableSingleObserver<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                viewState.postValue(ViewState.update(true));
            }

            @Override
            public void onError(Throwable e) {
                viewState.postValue(ViewState.error(e,true));
            }
        }, SaveCarro.Params.getParams(carro));
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
        deleteCarro.execute(new DisposableSingleObserver<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                viewState.postValue(ViewState.delete(true));
            }

            @Override
            public void onError(Throwable e) {
                viewState.postValue(ViewState.error(e,true));
            }
        }, DeleteCarro.Params.getParams(carro));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        saveCarro.dispose();
        deleteCarro.dispose();
    }
}
