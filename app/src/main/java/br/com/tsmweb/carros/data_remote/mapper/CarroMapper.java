package br.com.tsmweb.carros.data_remote.mapper;

import br.com.tsmweb.carros.data.mapper.Mapper;
import br.com.tsmweb.carros.data_remote.model.CarroEntity;
import br.com.tsmweb.carros.domain.model.Carro;

public final class CarroMapper implements Mapper<CarroEntity, Carro> {

    @Override
    public Carro toDomain(CarroEntity entity) {
        Carro c = new Carro();
        c.setTipo(entity.getTipo());
        c.setNome(entity.getNome());
        c.setDesc(entity.getDesc());
        c.setUrlFoto(entity.getUrlFoto());
        c.setUrlInfo(entity.getUrlInfo());
        c.setUrlVideo(entity.getUrlVideo());
        c.setLatitude(entity.getLatitude());
        c.setLongitude(entity.getLongitude());

        return c;
    }

    @Override
    public CarroEntity fromDomain(Carro domain) {
        CarroEntity c = new CarroEntity();
        c.setTipo(domain.getTipo());
        c.setNome(domain.getNome());
        c.setDesc(domain.getDesc());
        c.setUrlFoto(domain.getUrlFoto());
        c.setUrlInfo(domain.getUrlInfo());
        c.setUrlVideo(domain.getUrlVideo());
        c.setLatitude(domain.getLatitude());
        c.setLongitude(domain.getLongitude());

        return c;
    }

}
