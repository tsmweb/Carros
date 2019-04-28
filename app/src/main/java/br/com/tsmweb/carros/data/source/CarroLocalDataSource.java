package br.com.tsmweb.carros.data.source;

import java.util.List;

import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CarroLocalDataSource {

    Single<Long> save(Carro carro);

    Single<Integer> delete(Carro carro);

    Single<Integer> delete(List<Carro> carros);

    Single<Integer> deleteCarrosByTipo(String tipo);

    Flowable<List<Carro>> getCarrosByTipo(int tipo);

}
