package br.com.tsmweb.carros.data.source;

import java.util.List;

import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.Single;

public interface CarroRemoteDataSource {

    Single<List<Carro>> getCarros(int tipo);

}
