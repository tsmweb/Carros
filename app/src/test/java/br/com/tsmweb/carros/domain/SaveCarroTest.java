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

    private Carro dummyCarro;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        saveCarro = new SaveCarro(carroRepository, postExecutionThread);
        dummyCarro = DataFactory.makeDummyCarro(1);

        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void saveCarroCompletes() {
        stubSaveCarro(Single.just(1l));

        saveCarro.buildUseCaseSingle(SaveCarro.Params.getParams(dummyCarro))
                .test()
                .assertComplete();
    }

    @Test
    public void saveCarroSuccess() {
        stubSaveCarro(Single.just(1l));

        saveCarro.buildUseCaseSingle(SaveCarro.Params.getParams(dummyCarro))
                .test()
                .assertValue(it -> it == 1);
    }

    @Test
    public void saveCarroErrors() {
        Throwable err = new Throwable("Test Error");
        stubSaveCarro(Single.error(err));

        saveCarro.buildUseCaseSingle(SaveCarro.Params.getParams(dummyCarro))
                .test()
                .assertError(err);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveCarroThrowException() {
        saveCarro.buildUseCaseSingle(null)
                .test();
    }

    private void stubSaveCarro(Single single) {
        when(carroRepository.save(any(Carro.class))).thenReturn(single);
    }

}
