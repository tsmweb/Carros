package br.com.tsmweb.carros.domain.repository;

import java.util.List;

import br.com.tsmweb.carros.domain.Carro;
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
