package br.com.tsmweb.carros.ui.presentation.viewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import androidx.lifecycle.ViewModel;
import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarro;
import br.com.tsmweb.carros.domain.interactor.carros.SaveCarro;
import br.com.tsmweb.carros.domain.interactor.carros.ValidationCarro;
import br.com.tsmweb.carros.ui.presentation.common.SingleLiveEvent;
import br.com.tsmweb.carros.ui.presentation.mapper.CarroMapper;
import br.com.tsmweb.carros.ui.presentation.model.CarroBinding;
import io.reactivex.observers.DisposableSingleObserver;

public class CarroViewModel extends ViewModel {

    private static final String TAG = CarroViewModel.class.getSimpleName();

    private final SaveCarro saveCarro;
    private final DeleteCarro deleteCarro;
    private final ValidationCarro validationCarro;
    private final CarroMapper carroMapper;

    private MutableLiveData<ViewState<CarroBinding>> loadState = new MutableLiveData<>();
    private SingleLiveEvent<ViewState<Long>> updateState = new SingleLiveEvent<>();
    private SingleLiveEvent<ViewState<Integer>> deleteState = new SingleLiveEvent<>();
    private MutableLiveData<ViewState<String>> validationState = new MutableLiveData<>();

    private CarroBinding carro;

    public CarroViewModel(@NonNull SaveCarro saveCarro, @NonNull DeleteCarro deleteCarro,
                          @NonNull ValidationCarro validationCarro, @NonNull CarroMapper carroMapper) {
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
        // Verifica se os dados do carro são válidos
        validationCarro.execute(new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String field) {
                if (field != "OK") {
                    validationState.postValue(ViewState.viewState(ViewState.Status.INVALID, field));
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

            @Override
            public void onError(Throwable e) {
                validationState.postValue(ViewState.viewState(ViewState.Status.ERROR, e));
            }
        }, ValidationCarro.Params.getParams(carroMapper.toDomain(carro)));
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
