package br.com.tsmweb.carros.domain.interactor.carros;

import java.util.List;

import br.com.tsmweb.carros.domain.device.download.DownloadService;
import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.SingleUseCase;
import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.Single;

public class ShareCarros extends SingleUseCase<List<String>, ShareCarros.Params> {

    private final DownloadService downloadService;

    public ShareCarros(DownloadService downloadService, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.downloadService = downloadService;
    }

    @Override
    public Single<List<String>> buildUseCaseSingle(Params params) {
        if (params == null) throw new IllegalArgumentException("Params can't be null");

        return downloadService.onDownload(params.carros);
    }

    public static final class Params {

        private List<Carro> carros;

        private Params(List<Carro> carros) {
            this.carros = carros;
        }

        public static Params getParams(List<Carro> carros) {
            return new Params(carros);
        }
    }

}
