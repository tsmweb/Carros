package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.carros.UpdateCarros;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Completable;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UpdateCarrosTest {

    private UpdateCarros updateCarros;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private PostExecutionThread postExecutionThread;

    private int tipo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        updateCarros = new UpdateCarros(carroRepository, postExecutionThread);
        tipo = R.string.classicos;

        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void updateCarrosCompletes() {
        stubUpdateCarros(Completable.complete());

        updateCarros.buildUseCaseCompletable(UpdateCarros.Params.getParams(tipo))
                .test()
                .assertComplete();
    }

    @Test
    public void updateCarrosErros() {
        Throwable err = new Throwable("Test Error");
        stubUpdateCarros(Completable.error(err));

        updateCarros.buildUseCaseCompletable(UpdateCarros.Params.getParams(tipo))
                .test()
                .assertError(err);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateCarrorsThrowException() {
        stubUpdateCarros(Completable.complete());

        updateCarros.buildUseCaseCompletable(null)
                .test();
    }

    private void stubUpdateCarros(Completable completable) {
        when(carroRepository.updateCarros(tipo)).thenReturn(completable);
    }

}
