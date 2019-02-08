package br.com.tsmweb.carros.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarrosResponse {

    @SerializedName("carros")
    public CarroResult carros;

    public class CarroResult {

        @SerializedName("carro")
        public List<Carro> listCarro;

    }
}
