package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

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

    private Carro dummyCarro;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        getCarros = new GetCarros(carroRepository, postExecutionThread);
        dummyCarro = DataFactory.getDummyCarro();

        when(carroRepository.getCarrosByTipo(R.string.classicos)).thenReturn(
            Flowable.just(Collections.singletonList(dummyCarro))
        );
        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void getCarros() {
        getCarros.buildUseCaseFlowable(GetCarros.Params.getParams(R.string.classicos))
                .test()
                .assertValue(it -> it != null && it.contains(dummyCarro));
    }

}
