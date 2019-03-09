package br.com.tsmweb.carros.data_remote;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.data.source.CarroRemoteDataSource;
import br.com.tsmweb.carros.data_remote.mapper.CarroMapper;
import br.com.tsmweb.carros.data_remote.model.CarroEntity;
import br.com.tsmweb.carros.data_remote.model.CarrosResponse;
import br.com.tsmweb.carros.data_remote.service.CarrosService;
import br.com.tsmweb.carros.data_remote.source.CarroRemoteDataSourceImpl;
import io.reactivex.Single;

@RunWith(JUnit4.class)
public class CarroRemoteDataSourceTest {

    @Mock
    private CarrosService carrosService;

    private CarroRemoteDataSource carroRemoteDataSource;

    private CarrosResponse dummyCarroResponse;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        carroRemoteDataSource = new CarroRemoteDataSourceImpl(carrosService, new CarroMapper());

        List<CarroEntity> listCarro = new ArrayList<>();
        listCarro.add(DataFactory.getDummyCarroEntity());

        CarrosResponse.CarroResult carroResult = new CarrosResponse().new CarroResult();
        carroResult.listCarro = listCarro;

        dummyCarroResponse = new CarrosResponse();
        dummyCarroResponse.carros = carroResult;

        String tipo = "classicos";

        when(carrosService.getCarrosByTipo(tipo)).thenReturn(Single.just(dummyCarroResponse));
    }

    @Test
    public void getCarros() {
        carroRemoteDataSource.getCarros(R.string.classicos)
                .test()
                .assertValue(listCarro -> listCarro != null && listCarro.get(0).getNome().equals("Camaro"));
    }

}
