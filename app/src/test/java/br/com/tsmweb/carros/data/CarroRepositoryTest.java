package br.com.tsmweb.carros.data;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.data.repository.CarroRepositoryImpl;
import br.com.tsmweb.carros.data.source.CarroLocalDataSource;
import br.com.tsmweb.carros.data.source.CarroRemoteDataSource;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Flowable;
import io.reactivex.Single;

@RunWith(JUnit4.class)
public class CarroRepositoryTest {

    @Mock
    private CarroLocalDataSource carroLocalDataSource;

    @Mock
    private CarroRemoteDataSource carroRemoteDataSource;

    private CarroRepository carroRepository;

    private Carro dummyCarro;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        carroRepository = new CarroRepositoryImpl(carroLocalDataSource, carroRemoteDataSource);
        dummyCarro = DataFactory.getDummyCarro();
    }

    @Test
    public void saveCarro() {
        when(carroLocalDataSource.save(any(Carro.class))).thenReturn(Single.just(100L));

        carroRepository.save(dummyCarro)
                .test()
                .assertComplete()
                .assertValue(it -> it == 100);
    }

    @Test
    public void deleteCarro() {
        when(carroLocalDataSource.delete(any(Carro.class))).thenReturn(Single.just(1));

        carroRepository.delete(dummyCarro)
                .test()
                .assertComplete()
                .assertValue(it -> it == 1);
    }

    @Test
    public void deleteCarros() {
        when(carroLocalDataSource.delete(anyList())).thenReturn(Single.just(1));

        carroRepository.delete(Collections.singletonList(dummyCarro))
                .test()
                .assertComplete()
                .assertValue(it -> it == 1);
    }

    @Test
    public void getCarrosByTipo() {
        when(carroLocalDataSource.getCarrosByTipo(R.string.classicos)).thenReturn(
                Flowable.just(Collections.singletonList(dummyCarro))
        );

        carroRepository.getCarrosByTipo(R.string.classicos)
                .test()
                .assertComplete()
                .assertValue(listCarro -> listCarro != null && listCarro.contains(dummyCarro));
    }

    @Test
    public void updateCarros() {
        when(carroRemoteDataSource.getCarros(R.string.classicos)).thenReturn(
                Single.just(Collections.singletonList(dummyCarro))
        );

        when(carroLocalDataSource.save(any(Carro.class))).thenReturn(Single.just(100L));

        carroRepository.updateCarros(R.string.classicos)
                .test()
                .assertComplete()
                .assertComplete();
    }

}
