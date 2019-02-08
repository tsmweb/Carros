package br.com.tsmweb.carros.view.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import br.com.tsmweb.carros.data.Carro;
import br.com.tsmweb.carros.data.repository.ICarroRepository;
import br.com.tsmweb.carros.data.repository.RepositoryLocator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CarroViewModel extends AndroidViewModel {

    private static final String TAG = CarroViewModel.class.getSimpleName();

    private ICarroRepository carroRepository;

    public ObservableField<String> nome = new ObservableField<>();
    public ObservableField<String> descricao = new ObservableField<>();
    public ObservableField<String> urlFoto = new ObservableField<>();

    private MutableLiveData<ViewState<Boolean>> viewState = new MutableLiveData<>();

    private Carro carro;

    public CarroViewModel(@NonNull Application application) {
        super(application);

        // Obtém uma instância de ICarroRepository para manipular os dados dos carros
        this.carroRepository = RepositoryLocator
                .getInstance(application)
                .locate(ICarroRepository.class);
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
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(carroRepository.update(carro)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> compositeDisposable.clear())
                .subscribe(
                    ret -> {
                        viewState.postValue(ViewState.update(true));
                    },
                    err -> {
                        viewState.postValue(ViewState.error(err,true));
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
                        viewState.postValue(ViewState.delete(true));
                    },
                    err ->  {
                        viewState.postValue(ViewState.error(err,true));
                    }
                )
        );
    }

}
