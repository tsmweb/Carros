package br.com.tsmweb.carros.data_local.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.data.mapper.Mapper;
import br.com.tsmweb.carros.data.source.CarroLocalDataSource;
import br.com.tsmweb.carros.data_local.dao.CarroDAO;
import br.com.tsmweb.carros.data_local.model.CarroEntity;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.utils.AppUtils;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class CarroLocalDataSourceImpl implements CarroLocalDataSource {

    private final CarroDAO carroDAO;
    private final Mapper<CarroEntity, Carro> mapper;

    public CarroLocalDataSourceImpl(CarroDAO carroDAO, Mapper mapper) {
        this.carroDAO = carroDAO;
        this.mapper = mapper;
    }

    @Override
    public Single<Long> save(Carro carro) {
        return Single.fromCallable(() -> carroDAO.save(mapper.fromDomain(carro)));
    }

    @Override
    public Single<Integer> delete(Carro carro) {
        return Single.fromCallable(() -> carroDAO.delete(Collections.singletonList(mapper.fromDomain(carro))));
    }


    @Override
    public Single<Integer> delete(List<Carro> carros) {
        return Single.fromCallable(() -> carroDAO.delete(mapperFromDomain(carros)));
    }

    @Override
    public Flowable<List<Carro>> getCarrosByTipo(int tipo) {
        return carroDAO.getAllByTipo(AppUtils.getTipoCarroByResource(tipo))
                .map(ce -> mapperToDomain(ce));
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

    /*
     * Converte Carro para CarroEntity
     */
    private List<CarroEntity> mapperFromDomain(List<Carro> carros) {
        List<CarroEntity> carroEntities = new ArrayList<>();

        for (Carro c : carros) {
            carroEntities.add(mapper.fromDomain(c));
        }

        return carroEntities;
    }

}
