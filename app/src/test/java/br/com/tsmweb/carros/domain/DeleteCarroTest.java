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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        deleteCarro = new DeleteCarro(carroRepository, postExecutionThread);

        when(carroRepository.delete(any(Carro.class))).thenReturn(Single.just(1));
        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void execute() {
        deleteCarro.buildUseCaseSingle(DeleteCarro.Params.getParams(DataFactory.getDummyCarro()))
                .test()
                .assertValue(it -> it == 1);
    }

}
