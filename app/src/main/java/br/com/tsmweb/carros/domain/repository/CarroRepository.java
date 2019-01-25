package br.com.tsmweb.carros.domain.repository;

import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.CarrosAPI;
import br.com.tsmweb.carros.domain.dao.CarroDAO;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class CarroRepository implements ICarroRepository {

    private static final String TAG = CarroRepository.class.getSimpleName();

    private final CarroDAO carroDAO;

    public CarroRepository(CarroDAO carroDAO) {
        this.carroDAO = carroDAO;
    }

    @Override
    public Single<Long> add(Carro carro) {
        return Single.fromCallable(() -> carroDAO.save(carro));
    }

    @Override
    public Single<Long> update(Carro carro) {
        return Single.fromCallable(() -> carroDAO.save(carro));
    }

    @Override
    public Single<Long> save(Carro item) {
        return Single.fromCallable(() -> carroDAO.save(item));
    }

    @Override
    public Single<Integer> delete(Carro carro) {
        return Single.fromCallable(() -> carroDAO.delete(Collections.singletonList(carro)));
    }

    @Override
    public Single<Integer> delete(List<Carro> carros) {
        return Single.fromCallable(() -> carroDAO.delete(carros));
    }

    @Override
    public Maybe<Carro> getById(Long id) {
        return Maybe.fromCallable(() -> carroDAO.getById(id));
    }

    @Override
    public Flowable<List<Carro>> getAll() {
        return carroDAO.getAll();
    }

    @Override
    public Flowable<List<Carro>> getCarrosByTipo(int tipo) {
        Log.d(TAG, "CarroRepository.getCarros()");

        return getCarrosFromBanco(tipo)
                .doOnNext(carros -> {
                    if (carros.isEmpty()) {
                        downloadCarrosFromAPI(tipo).subscribe();
                    }
                });
    }

    private Flowable<List<Carro>> getCarrosFromBanco(int tipo) {
        return carroDAO.getAllByTipo(getTipo(tipo));
    }

    @Override
    public Completable downloadCarrosFromAPI(int tipo) {
        String tipoStr = getTipo(tipo);

        return Completable.create(emitter -> {
               try {
                   List<Carro> carros = CarrosAPI.getCarrosFromWebService(tipo);
                   carroDAO.deleteCarrosByTipo(tipoStr);

                   Observable.fromIterable(carros)
                           .subscribe(carro -> {
                               carro.setTipo(tipoStr);
                               long id = carroDAO.save(carro);
                               carro.setId(id);
                               Log.d(TAG, "Salvando o carro " + carro.getId() + " - "+ carro.getNome());
                            });

                   emitter.onComplete();
               } catch (IOException ex) {
                   emitter.onError(new Throwable(ex));
               }
        });
    }

    // Converte a constante para string, para criar a URL do web service.
    private String getTipo(int tipo) {
        if (tipo == R.string.classicos) {
            return "classicos";
        }

        if (tipo == R.string.esportivos) {
            return "esportivos";
        }

        return "luxo";
    }

}
