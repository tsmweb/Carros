package br.com.tsmweb.carros.data.api;

import br.com.tsmweb.carros.data.CarrosResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CarrosService {

    @GET("carros_{tipo}.json")
    Single<CarrosResponse> getCarrosByTipo(@Path("tipo") String tipo);

}
