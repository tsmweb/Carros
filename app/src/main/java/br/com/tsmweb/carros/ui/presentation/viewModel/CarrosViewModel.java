package br.com.tsmweb.carros.ui.presentation.viewModel;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarros;
import br.com.tsmweb.carros.domain.interactor.carros.GetCarros;
import br.com.tsmweb.carros.domain.interactor.carros.ShareCarros;
import br.com.tsmweb.carros.domain.interactor.carros.UpdateCarros;
import br.com.tsmweb.carros.ui.presentation.common.SingleLiveEvent;
import br.com.tsmweb.carros.ui.presentation.mapper.CarroMapper;
import br.com.tsmweb.carros.ui.presentation.model.CarroBinding;
import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.Observable;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.DisposableSubscriber;

public class CarrosViewModel extends AndroidViewModel {

    private static final String TAG = CarroViewModel.class.getSimpleName();

    private final GetCarros getCarros;
    private final UpdateCarros updateCarros;
    private final DeleteCarros deleteCarros;
    private final ShareCarros shareCarros;
    private final CarroMapper carroMapper;

    private List<CarroBinding> listCarros;

    private MutableLiveData<ViewState<List<CarroBinding>>> loadState = new MutableLiveData<>();
    private MutableLiveData<ViewState> updateState = new MutableLiveData<>();
    private SingleLiveEvent<ViewState<Integer>> deleteState = new SingleLiveEvent<>();
    private SingleLiveEvent<ViewState<List<String>>> shareState = new SingleLiveEvent<>();

    private SingleLiveEvent<CarroBinding> checkedCarro = new SingleLiveEvent<>();
    private MutableLiveData<CarroBinding> checkedLongCarro = new MutableLiveData<>();

    private int countCheckedCarro = 0;

    public CarrosViewModel(@NonNull Application application, @NonNull GetCarros getCarros, @NonNull UpdateCarros updateCarros,
                           @NonNull DeleteCarros deleteCarros, @NonNull ShareCarros shareCarros, @NonNull CarroMapper carroMapper) {
        super(application);
        this.getCarros = getCarros;
        this.updateCarros = updateCarros;
        this.deleteCarros = deleteCarros;
        this.shareCarros = shareCarros;
        this.carroMapper = carroMapper;
    }

    public LiveData<ViewState<List<CarroBinding>>> getLoadState() {
        return loadState;
    }

    public LiveData<ViewState> getUpdateState() {
        return updateState;
    }

    public LiveData<ViewState<Integer>> getDeleteState() {
        return deleteState;
    }

    public LiveData<ViewState<List<String>>> getShareState() {
        return shareState;
    }

    public LiveData<CarroBinding> getCheckedCarro() {
        return checkedCarro;
    }

    public LiveData<CarroBinding> getCheckedLongCarro() {
        return checkedLongCarro;
    }

    public int getCountCheckedCarro() {
        return countCheckedCarro;
    }

    // Carrega os carros da base de dados pelo tipo
    public void loadCarros(int tipo) {
        loadState.postValue(ViewState.viewState(ViewState.Status.LOADING));

        getCarros.execute(new DisposableSubscriber<List<Carro>>() {
            @Override
            public void onNext(List<Carro> carros) {
                List<CarroBinding> carrosBinding = Observable.fromIterable(carros)
                        .map(c -> carroMapper.fromDomain(c))
                        .toList()
                        .blockingGet();

                listCarros = carrosBinding;
                loadState.postValue(ViewState.viewState(ViewState.Status.SUCCESS, carrosBinding));
            }

            @Override
            public void onError(Throwable e) {
                loadState.postValue(ViewState.viewState(ViewState.Status.ERROR, e));
            }

            @Override
            public void onComplete() {
                throw new OnErrorNotImplementedException(new Throwable("method not implemented"));
            }
        }, GetCarros.Params.getParams(tipo));
    }

    // Obtém os carros de um web-services e guarda na base de dados local
    public void updateCarros(int tipo) {
        updateState.postValue(ViewState.viewState(ViewState.Status.LOADING));

        updateCarros.execute(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                updateState.postValue(ViewState.viewState(ViewState.Status.SUCCESS));
            }

            @Override
            public void onError(Throwable e) {
                updateState.postValue(ViewState.viewState(ViewState.Status.ERROR, e));
            }
        }, UpdateCarros.Params.getParams(tipo));
    }

    // Deleta os carros selecionados
    public void deleteCheckedCarros() {
        deleteCarros.execute(new DisposableSingleObserver<Integer>() {
            @Override
            public void onSuccess(Integer qtde) {
                countCheckedCarro -= qtde;
                deleteState.postValue(ViewState.viewState(ViewState.Status.SUCCESS));
            }

            @Override
            public void onError(Throwable e) {
                deleteState.postValue(ViewState.viewState(ViewState.Status.ERROR, e));
            }
        }, DeleteCarros.Params.getParams(getCheckedCarros()));
    }

    // Retorna a lista de carros selecionados
    private List<Carro> getCheckedCarros() {
        return Observable.fromIterable(listCarros)
                .filter(c -> c.selected)
                .map(c -> carroMapper.toDomain(c))
                .toList()
                .blockingGet();
    }

    // Seleciona um carro com o evento de click
    public void onCarroItemClick(CarroBinding carro) {
        if (countCheckedCarro > 0) {
            carro.selected = !carro.selected;

            if (carro.selected) {
                countCheckedCarro++;
            }  else {
                countCheckedCarro--;
            }
        }

        checkedCarro.postValue(carro);
    }

    // Seleciona um carro com o evento de click longo
    public boolean onCarroItemLongClick(CarroBinding carro) {
        uncheckCarros();
        carro.selected = !carro.selected;

        if (carro.selected) {
            countCheckedCarro++;
        }  else {
            countCheckedCarro--;
        }

        checkedLongCarro.postValue(carro);

        return true;
    }

    // Configura todos os carros para não selecionados
    public void uncheckCarros() {
        if (countCheckedCarro == 0) { return; }

        for (CarroBinding c : listCarros) {
            c.selected = false;
        }

        countCheckedCarro = 0;
    }

    // Compartilha os carros selecionados
    public void shareSelectedCarros() {
        shareState.postValue(ViewState.viewState(ViewState.Status.LOADING));

        shareCarros.execute(new DisposableSingleObserver<List<String>>() {
            @Override
            public void onSuccess(List<String> paths) {
                shareState.postValue(ViewState.viewState(ViewState.Status.SUCCESS, paths));
            }

            @Override
            public void onError(Throwable e) {
                shareState.postValue(ViewState.viewState(ViewState.Status.ERROR, e));
            }
        }, ShareCarros.Params.getParams(getCheckedCarros()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        getCarros.dispose();
        updateCarros.dispose();
        deleteCarros.dispose();
        shareCarros.dispose();
    }
}
