package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.carros.ValidationCarro;
import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ValidationTest {

    private ValidationCarro validationCarro;

    @Mock
    private PostExecutionThread postExecutionThread;

    private Carro dummyCarro;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        validationCarro = new ValidationCarro(postExecutionThread);
        dummyCarro = DataFactory.makeDummyCarro(1);

        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void validationCarroInvalid() {
        dummyCarro.setNome(null);
        validationCarro.buildUseCaseSingle(ValidationCarro.Params.getParams(dummyCarro))
                .test()
                .assertValue("nome");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validationCarroThrowException() {
        validationCarro.buildUseCaseSingle(null)
                .test();
    }

}
