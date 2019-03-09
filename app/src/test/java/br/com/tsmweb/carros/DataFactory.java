package br.com.tsmweb.carros;

import java.util.UUID;

import br.com.tsmweb.carros.data_remote.model.CarroEntity;
import br.com.tsmweb.carros.domain.model.Carro;

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
