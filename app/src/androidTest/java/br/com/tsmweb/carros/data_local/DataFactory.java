package br.com.tsmweb.carros.data_local;

import java.util.UUID;

import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.data.data_local.model.CarroEntity;

public class DataFactory {

    public static Carro getDummyCarro() {
        Carro dummyCarro = new Carro();
        dummyCarro.setId(100);
        dummyCarro.setTipo("classicos");
        dummyCarro.setNome("Camaro");
        dummyCarro.setDesc(UUID.randomUUID().toString() + " - Camaro");
        dummyCarro.setUrlFoto(UUID.randomUUID().toString());
        dummyCarro.setUrlVideo(UUID.randomUUID().toString());
        dummyCarro.setUrlInfo(UUID.randomUUID().toString());
        dummyCarro.setLatitude(UUID.randomUUID().toString());
        dummyCarro.setLongitude(UUID.randomUUID().toString());

        return dummyCarro;
    }

    public static CarroEntity getDummyCarroEntity() {
        CarroEntity dummyCarro = new CarroEntity();
        dummyCarro.setTipo("classicos");
        dummyCarro.setNome("Camaro");
        dummyCarro.setDesc(UUID.randomUUID().toString() + " - Camaro");
        dummyCarro.setUrlFoto(UUID.randomUUID().toString());
        dummyCarro.setUrlVideo(UUID.randomUUID().toString());
        dummyCarro.setUrlInfo(UUID.randomUUID().toString());
        dummyCarro.setLatitude(UUID.randomUUID().toString());
        dummyCarro.setLongitude(UUID.randomUUID().toString());

        return dummyCarro;
    }

}
