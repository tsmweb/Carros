package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.carros.SaveCarro;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SaveCarroTest {

    private SaveCarro saveCarro;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private PostExecutionThread postExecutionThread;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        saveCarro = new SaveCarro(carroRepository, postExecutionThread);

        when(carroRepository.save(any(Carro.class))).thenReturn(Single.just(100l));
        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void saveCarro() {
        saveCarro.buildUseCaseSingle(SaveCarro.Params.getParams(DataFactory.getDummyCarro()))
                .test()
                .assertValue(it -> it == 100);
    }

}
