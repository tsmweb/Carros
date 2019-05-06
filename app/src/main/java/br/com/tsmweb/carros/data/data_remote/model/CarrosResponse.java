package br.com.tsmweb.carros.data.data_remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarrosResponse {

    @SerializedName("carros")
    public CarroResult carros;

    public class CarroResult {

        @SerializedName("carro")
        public List<CarroEntity> listCarro;

    }
}
