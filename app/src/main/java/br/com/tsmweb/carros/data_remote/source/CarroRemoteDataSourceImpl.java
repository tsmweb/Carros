package br.com.tsmweb.carros.data_remote.source;

import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.data.mapper.Mapper;
import br.com.tsmweb.carros.data.source.CarroRemoteDataSource;
import br.com.tsmweb.carros.data_remote.model.CarroEntity;
import br.com.tsmweb.carros.data_remote.service.CarrosService;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.utils.AppUtils;
import io.reactivex.Single;

public class CarroRemoteDataSourceImpl implements CarroRemoteDataSource {

    private static final String TAG = CarroRemoteDataSourceImpl.class.getSimpleName();

    private final CarrosService carrosService;
    private final Mapper<CarroEntity, Carro> mapper;

    public CarroRemoteDataSourceImpl(CarrosService carrosService, Mapper mapper) {
        this.carrosService = carrosService;
        this.mapper = mapper;
    }

    @Override
    public Single<List<Carro>> getCarros(int tipo) {
        return carrosService
                .getCarrosByTipo(AppUtils.getTipoCarroByResource(tipo))
                .map(cr -> mapperToDomain(cr.carros.listCarro));
    }

    /*
     * Converte CarroEntity para Carro
     */
    private List<Carro> mapperToDomain(List<CarroEntity> carroEntities) {
        List<Carro> carros = new ArrayList<>();

        for (CarroEntity ce : carroEntities) {
            carros.add(mapper.toDomain(ce));
        }

        return carros;
    }

}
