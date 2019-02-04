package br.com.tsmweb.carros.domain.repository;

import android.util.Log;

import java.util.Collections;
import java.util.List;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.api.CarrosApi;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.domain.dao.CarroDAO;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class CarroRepository implements ICarroRepository {

    private static final String TAG = CarroRepository.class.getSimpleName();

    private final CarroDAO carroDAO;
    private boolean isDownloading;

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
                    if (carros.isEmpty() && !isDownloading) {
                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        downloadCarrosFromAPI(tipo)
                                .doAfterTerminate(() -> compositeDisposable.clear())
                                .subscribe();
                    }
                });
    }

    private Flowable<List<Carro>> getCarrosFromBanco(int tipo) {
        return carroDAO.getAllByTipo(getTipo(tipo));
    }

    @Override
    public Completable downloadCarrosFromAPI(int tipo) {
        String tipoStr = getTipo(tipo);

        return Completable.fromCallable(() ->
            new CarrosApi().getCarrosService()
                    .getCarrosByTipo(tipoStr)
                    .map(carrosResult -> carrosResult.carros.listCarro)
                    .subscribe(
                            carros -> {
                                isDownloading = true;
                                carroDAO.deleteCarrosByTipo(tipoStr);

                                for (Carro carro : carros) {
                                    carro.setTipo(tipoStr);
                                    long id = carroDAO.save(carro);
                                    carro.setId(id);
                                    Log.d(TAG, "Salvando o carro " + carro.getId() + " - " + carro.getNome());
                                }

                                isDownloading = false;
                            }
                    )
        );
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
