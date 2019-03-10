package br.com.tsmweb.carros.data_remote;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.data.source.CarroRemoteDataSource;
import br.com.tsmweb.carros.data_remote.mapper.CarroMapper;
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
    private int tipo;
    private String tipoStr;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        carroRemoteDataSource = new CarroRemoteDataSourceImpl(carrosService, new CarroMapper());
        dummyCarroResponse = DataFactory.makeDummyCarroResponse();
        tipo = R.string.classicos;
        tipoStr = "classicos";
    }

    @Test
    public void getCarrosCompletes() {
        stubGetCarros(Single.just(dummyCarroResponse));

        carroRemoteDataSource.getCarros(tipo)
                .test()
                .assertComplete();
    }

    @Test
    public void getCarrosSuccess() {
        stubGetCarros(Single.just(dummyCarroResponse));

        carroRemoteDataSource.getCarros(tipo)
                .test()
                .assertValue(it -> it != null && it.size() > 0);
    }

    @Test
    public void getCarrosErrors() {
        Throwable err = new Throwable("Test Error");
        stubGetCarros(Single.error(err));

        carroRemoteDataSource.getCarros(tipo)
                .test()
                .assertError(err);
    }

    private void stubGetCarros(Single single) {
        when(carrosService.getCarrosByTipo(tipoStr)).thenReturn(single);
    }

}
