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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        updateCarros = new UpdateCarros(carroRepository, postExecutionThread);

        when(carroRepository.updateCarros(R.string.classicos)).thenReturn(Completable.complete());
        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void updateCarros() {
        updateCarros.buildUseCaseCompletable(UpdateCarros.Params.getParams(R.string.classicos))
                .test()
                .assertComplete();
    }

}
