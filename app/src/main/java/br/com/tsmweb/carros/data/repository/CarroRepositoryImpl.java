package br.com.tsmweb.carros.data.repository;

import android.util.Log;

import java.util.List;

import br.com.tsmweb.carros.data.source.CarroLocalDataSource;
import br.com.tsmweb.carros.data.source.CarroRemoteDataSource;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import br.com.tsmweb.carros.utils.AppUtils;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class CarroRepositoryImpl implements CarroRepository {

    private static final String TAG = CarroRepositoryImpl.class.getSimpleName();

    private final CarroLocalDataSource carroLocalDataSource;
    private final CarroRemoteDataSource carroRemoteDataSource;

    public CarroRepositoryImpl(CarroLocalDataSource carroLocalDataSource, CarroRemoteDataSource carroRemoteDataSource) {
        this.carroLocalDataSource = carroLocalDataSource;
        this.carroRemoteDataSource = carroRemoteDataSource;
    }

    @Override
    public Single<Long> save(Carro carro) {
        return carroLocalDataSource.save(carro);
    }

    @Override
    public Single<Integer> delete(Carro carro) {
        return carroLocalDataSource.delete(carro);
    }

    @Override
    public Single<Integer> delete(List<Carro> carros) {
        return carroLocalDataSource.delete(carros);
    }

    @Override
    public Flowable<List<Carro>> getCarrosByTipo(int tipo) {
        return carroLocalDataSource.getCarrosByTipo(tipo);
        /*
                .doOnNext(carros -> {
                    if (carros.isEmpty()) {
                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        compositeDisposable.add(
                                updateCarros(tipo)
                                    .doAfterTerminate(() -> compositeDisposable.clear())
                                    .subscribe()
                        );
                    }
                });
        */
    }

    @Override
    public Completable updateCarros(int tipo) {
        return Completable.create(emitter -> {
            String tipoStr = AppUtils.getTipoCarroByResource(tipo);

            try {
                Disposable disposable = carroRemoteDataSource.getCarros(tipo)
                        .subscribe(carros -> {
                            for (Carro carro : carros) {
                                carro.setTipo(tipoStr);
                                carroLocalDataSource.save(carro).subscribe();
                                    //.subscribe(
                                    //        id -> Log.d(TAG, "Salvando o carro " + id +" - "+ carro.getNome()),
                                    //        err -> emitter.onError(err));
                            }
                        });

                emitter.setDisposable(Disposables.fromAction(() -> disposable.dispose()));
                emitter.onComplete();
            } catch(Exception e) {
                emitter.onError(e);
            }
        });
    }

}
