package br.com.tsmweb.carros.presentation.mapper;

import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.presentation.model.CarroBinding;

public class CarroMapper implements Mapper<Carro, CarroBinding> {

    @Override
    public Carro toDomain(CarroBinding binding) {
        Carro c = new Carro();
        c.setId(binding.getId());
        c.setTipo(binding.getTipo());
        c.setNome(binding.getNome());
        c.setDesc(binding.getDesc());
        c.setUrlFoto(binding.getUrlFoto());
        c.setUrlInfo(binding.getUrlInfo());
        c.setUrlVideo(binding.getUrlVideo());
        c.setLatitude(binding.getLatitude());
        c.setLongitude(binding.getLongitude());

        return c;
    }

    @Override
    public CarroBinding fromDomain(Carro domain) {
        CarroBinding cb = new CarroBinding();
        cb.setId(domain.getId());
        cb.setTipo(domain.getTipo());
        cb.setNome(domain.getNome());
        cb.setDesc(domain.getDesc());
        cb.setUrlFoto(domain.getUrlFoto());
        cb.setUrlInfo(domain.getUrlInfo());
        cb.setUrlVideo(domain.getUrlVideo());
        cb.setLatitude(domain.getLatitude());
        cb.setLongitude(domain.getLongitude());

        return cb;
    }

}
