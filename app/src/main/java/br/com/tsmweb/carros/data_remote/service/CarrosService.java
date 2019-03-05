package br.com.tsmweb.carros.data_remote.service;

import br.com.tsmweb.carros.data_remote.model.CarrosResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CarrosService {

    @GET("carros_{tipo}.json")
    Single<CarrosResponse> getCarrosByTipo(@Path("tipo") String tipo);

}
