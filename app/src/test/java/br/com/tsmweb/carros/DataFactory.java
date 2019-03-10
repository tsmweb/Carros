package br.com.tsmweb.carros;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.tsmweb.carros.data_remote.model.CarroEntity;
import br.com.tsmweb.carros.data_remote.model.CarrosResponse;
import br.com.tsmweb.carros.domain.model.Carro;

public class DataFactory {

    public static Carro makeDummyCarro(int id) {
        Carro dummyCarro = new Carro();
        dummyCarro.setId(id);
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

    public static List<Carro> makeDummyListCarro(int c) {
        List<Carro> carros = new ArrayList<>();

        for (int i = 0; i < c; i++) {
            carros.add(makeDummyCarro(i));
        }

        return carros;
    }

    public static CarroEntity makeDummyCarroEntity() {
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

    public static List<CarroEntity> makeDummyListCarroEntity(int c) {
        List<CarroEntity> carros = new ArrayList<>();

        for (int i = 0; i < c; i++) {
            carros.add(makeDummyCarroEntity());
        }

        return carros;
    }

    public static CarrosResponse makeDummyCarroResponse() {
        List<CarroEntity> listCarro = new ArrayList<>();
        listCarro.add(DataFactory.makeDummyCarroEntity());

        CarrosResponse.CarroResult carroResult = new CarrosResponse().new CarroResult();
        carroResult.listCarro = listCarro;

        CarrosResponse dummyCarroResponse = new CarrosResponse();
        dummyCarroResponse.carros = carroResult;

        return dummyCarroResponse;
    }

}
