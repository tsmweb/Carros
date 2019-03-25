package br.com.tsmweb.carros.presentation.viewModel;

import android.app.Application;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarro;
import br.com.tsmweb.carros.domain.interactor.carros.SaveCarro;
import br.com.tsmweb.carros.domain.interactor.carros.ValidationCarro;
import br.com.tsmweb.carros.presentation.mapper.CarroMapper;
import br.com.tsmweb.carros.presentation.model.CarroBinding;
import io.reactivex.observers.DisposableSingleObserver;

public class CarroViewModel extends AndroidViewModel {

    private static final String TAG = CarroViewModel.class.getSimpleName();

    private final SaveCarro saveCarro;
    private final DeleteCarro deleteCarro;
    private final ValidationCarro validationCarro;
    private final CarroMapper carroMapper;

    private MutableLiveData<ViewState<CarroBinding>> loadState = new MutableLiveData<>();
    private MutableLiveData<ViewState<Long>> updateState = new MutableLiveData<>();
    private MutableLiveData<ViewState<Integer>> deleteState = new MutableLiveData<>();
    private MutableLiveData<ViewState<String>> validationState = new MutableLiveData<>();

    private CarroBinding carro;

    public CarroViewModel(@NonNull Application application, @NonNull SaveCarro saveCarro, @NonNull DeleteCarro deleteCarro,
                          @NonNull ValidationCarro validationCarro, @NonNull CarroMapper carroMapper) {
        super(application);

        this.saveCarro = saveCarro;
        this.deleteCarro = deleteCarro;
        this.validationCarro = validationCarro;
        this.carroMapper = carroMapper;
    }

    public void setCarro(CarroBinding carro) {
        this.carro = carro;
        loadState.postValue(ViewState.viewState(ViewState.Status.SUCCESS, carro));
    }

    public LiveData<ViewState<CarroBinding>> getLoadState() {
        return loadState;
    }

    public LiveData<ViewState<Long>> getUpdateState() {
        return updateState;
    }

    public LiveData<ViewState<Integer>> getDeleteState() {
        return deleteState;
    }

    public LiveData<ViewState<String>> getValidationState() {
        return validationState;
    }

    public void onCarroUpdate() {
        AtomicBoolean isValid = new AtomicBoolean(true);

        // Verifica se os dados do carro são válidos
        validationCarro.execute(new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String field) {
                if (field != null) {
                    isValid.set(false);
                    validationState.postValue(ViewState.viewState(ViewState.Status.INVALID, field));
                }
            }

            @Override
            public void onError(Throwable e) {
                updateState.postValue(ViewState.viewState(ViewState.Status.ERROR, e));
            }
        }, ValidationCarro.Params.getParams(carroMapper.toDomain(carro)));

        if (!isValid.get()) {
            return;
        }

        // Salva as alterações no banco de dados
        saveCarro.execute(new DisposableSingleObserver<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                updateState.postValue(ViewState.viewState(ViewState.Status.SUCCESS, aLong));
            }

            @Override
            public void onError(Throwable e) {
                updateState.postValue(ViewState.viewState(ViewState.Status.ERROR, e));
            }
        }, SaveCarro.Params.getParams(carroMapper.toDomain(carro)));
    }

    public void onCarroDelete() {
        // Deleta o carro
        deleteCarro.execute(new DisposableSingleObserver<Integer>() {
            @Override
            public void onSuccess(Integer rows) {
                deleteState.postValue(ViewState.viewState(ViewState.Status.SUCCESS, rows));
            }

            @Override
            public void onError(Throwable e) {
                deleteState.postValue(ViewState.viewState(ViewState.Status.ERROR, e));
            }
        }, DeleteCarro.Params.getParams(carroMapper.toDomain(carro)));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        validationCarro.dispose();
        saveCarro.dispose();
        deleteCarro.dispose();
    }
}
