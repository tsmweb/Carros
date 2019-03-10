package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarro;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

@RunWith(JUnit4.class)
public class DeleteCarroTest {

    private DeleteCarro deleteCarro;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private PostExecutionThread postExecutionThread;

    private Carro dummyCarro;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        deleteCarro = new DeleteCarro(carroRepository, postExecutionThread);
        dummyCarro = DataFactory.makeDummyCarro(1);

        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void deleteCarroCompletes() {
        stubDeleteCarro(Single.just(1));

        deleteCarro.buildUseCaseSingle(DeleteCarro.Params.getParams(dummyCarro))
                .test()
                .assertComplete();
    }

    @Test
    public void deleteCarroSuccess() {
        stubDeleteCarro(Single.just(1));

        deleteCarro.buildUseCaseSingle(DeleteCarro.Params.getParams(dummyCarro))
                .test()
                .assertValue(it -> it == 1);
    }

    @Test
    public void deleteCarroErrors() {
        Throwable err = new Throwable("Test Error");
        stubDeleteCarro(Single.error(err));

        deleteCarro.buildUseCaseSingle(DeleteCarro.Params.getParams(dummyCarro))
                .test()
                .assertError(err);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteCarroThrowException() {
        deleteCarro.buildUseCaseSingle(null)
                .test();
    }

    private void stubDeleteCarro(Single single) {
        when(carroRepository.delete(any(Carro.class))).thenReturn(single);
    }

}
