package br.com.tsmweb.carros.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarrosResult {

    @SerializedName("carros")
    public CarroResult carros;

    public class CarroResult {

        @SerializedName("carro")
        public List<Carro> listCarro;

    }
}
