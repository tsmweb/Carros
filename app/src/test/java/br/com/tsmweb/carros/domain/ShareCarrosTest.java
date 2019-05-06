package br.com.tsmweb.carros.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.domain.device.download.DownloadService;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarros;
import br.com.tsmweb.carros.domain.interactor.carros.ShareCarros;
import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ShareCarrosTest {

    private ShareCarros shareCarros;

    @Mock
    private DownloadService downloadService;

    @Mock
    private PostExecutionThread postExecutionThread;

    private List<Carro> dummyCarros;
    private List<String> dummyPath;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        shareCarros = new ShareCarros(downloadService, postExecutionThread);
        dummyCarros = DataFactory.makeDummyListCarro(3);
        dummyPath = DataFactory.makeDummyListPath(3);

        when(postExecutionThread.getScheduler()).thenReturn(new TestScheduler());
    }

    @Test
    public void shareCarrosCompletes() {
        stubShareCarros(Single.just(dummyPath));

        shareCarros.buildUseCaseSingle(ShareCarros.Params.getParams(dummyCarros))
                .test();
    }

    @Test
    public void shareCarrosSuccess() {
        stubShareCarros(Single.just(dummyPath));

        shareCarros.buildUseCaseSingle(ShareCarros.Params.getParams(dummyCarros))
                .test()
                .assertValue(it -> it.size() == dummyCarros.size());
    }

    @Test
    public void shareCarrosErrors() {
        Throwable err = new Throwable("Test Error");
        stubShareCarros(Single.error(err));

        shareCarros.buildUseCaseSingle(ShareCarros.Params.getParams(dummyCarros))
                .test()
                .assertError(err);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shareCarrosThrowException() {
        shareCarros.buildUseCaseSingle(null)
                .test();
    }

    private void stubShareCarros(Single single) {
        when(downloadService.onDownload(anyList())).thenReturn(single);
    }

}
