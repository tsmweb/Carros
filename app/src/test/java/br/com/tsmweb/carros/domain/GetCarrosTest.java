package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.carros.GetCarros;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Flowable;
import io.reactivex.schedulers.TestScheduler;

@RunWith(JUnit4.class)
public class GetCarrosTest {

    private GetCarros getCarros;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private PostExecutionThread postExecutionThread;

    private List<Carro> dummyCarros;
    private int tipo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        getCarros = new GetCarros(carroRepository, postExecutionThread);
        dummyCarros = DataFactory.makeDummyListCarro(3);
        tipo = R.string.classicos;

        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void getCarrosCompletes() {
        stubGetCarros(Flowable.just(dummyCarros));

        getCarros.buildUseCaseFlowable(GetCarros.Params.getParams(tipo))
                .test()
                .assertComplete();
    }

    @Test
    public void getCarrosReturnsData() {
        stubGetCarros(Flowable.just(dummyCarros));

        getCarros.buildUseCaseFlowable(GetCarros.Params.getParams(tipo))
                .test()
                .assertValue(dummyCarros);
    }

    @Test
    public void getCarrosErrors() {
        Throwable err = new Throwable("Test Error");
        stubGetCarros(Flowable.error(err));

        getCarros.buildUseCaseFlowable(GetCarros.Params.getParams(tipo))
                .test()
                .assertError(err);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCarrosThrowException() {
        getCarros.buildUseCaseFlowable(null)
                .test();
    }

    private void stubGetCarros(Flowable flowable) {
        when(carroRepository.getCarrosByTipo(tipo)).thenReturn(flowable);
    }

}
