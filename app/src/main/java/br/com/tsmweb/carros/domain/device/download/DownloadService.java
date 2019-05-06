package br.com.tsmweb.carros.domain.device.download;

import java.util.List;

import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.Single;

public interface DownloadService {

    Single<List<String>> onDownload(List<Carro> carros);

}
