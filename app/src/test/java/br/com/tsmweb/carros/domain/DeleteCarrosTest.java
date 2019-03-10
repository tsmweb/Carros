package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarros;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DeleteCarrosTest {

    private DeleteCarros deleteCarros;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private PostExecutionThread postExecutionThread;

    private List<Carro> dummyCarros;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        deleteCarros = new DeleteCarros(carroRepository, postExecutionThread);
        dummyCarros = DataFactory.makeDummyListCarro(3);

        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void deleteCarrosCompletes() {
        stubDeleteCarros(Single.just(dummyCarros.size()));

        deleteCarros.buildUseCaseSingle(DeleteCarros.Params.getParams(dummyCarros))
                .test();
    }

    @Test
    public void deleteCarrosSuccess() {
        stubDeleteCarros(Single.just(dummyCarros.size()));

        deleteCarros.buildUseCaseSingle(DeleteCarros.Params.getParams(dummyCarros))
                .test()
                .assertValue(it -> it == dummyCarros.size());
    }

    @Test
    public void deleteCarrosErrors() {
        Throwable err = new Throwable("Test Error");
        stubDeleteCarros(Single.error(err));

        deleteCarros.buildUseCaseSingle(DeleteCarros.Params.getParams(dummyCarros))
                .test()
                .assertError(err);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteCarrosThrowException() {
        deleteCarros.buildUseCaseSingle(null)
                .test();
    }

    private void stubDeleteCarros(Single single) {
        when(carroRepository.delete(anyList())).thenReturn(single);
    }

}
