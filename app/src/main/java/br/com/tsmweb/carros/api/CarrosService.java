package br.com.tsmweb.carros.api;

import br.com.tsmweb.carros.domain.CarrosResult;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CarrosService {

    @GET("carros_{tipo}.json")
    Single<CarrosResult> getCarrosByTipo(@Path("tipo") String tipo);

}
