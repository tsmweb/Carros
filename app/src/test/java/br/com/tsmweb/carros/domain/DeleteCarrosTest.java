package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarros;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DeleteCarrosTest {

    private DeleteCarros deleteCarros;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private PostExecutionThread postExecutionThread;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        deleteCarros = new DeleteCarros(carroRepository, postExecutionThread);

        when(carroRepository.delete(anyList())).thenReturn(Single.just(1));
        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void execute() {
        deleteCarros.buildUseCaseSingle(DeleteCarros.Params.getParams(Collections.singletonList(DataFactory.getDummyCarro())))
                .test()
                .assertValue(it -> it == 1);
    }

}
