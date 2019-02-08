package br.com.tsmweb.carros.data.repository;

import java.util.List;

import br.com.tsmweb.carros.data.Carro;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ICarroRepository extends IRepository<Carro, Long> {

    Single<Long> add(Carro carro);

    Single<Long> update(Carro carro);

    Single<Integer> delete(List<Carro> carros);

    Flowable<List<Carro>> getCarrosByTipo(int tipo);

    Completable downloadCarrosFromAPI(int tipo);

}
