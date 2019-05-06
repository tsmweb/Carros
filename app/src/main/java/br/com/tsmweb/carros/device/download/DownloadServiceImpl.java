package br.com.tsmweb.carros.device.download;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.domain.device.download.DownloadService;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.utils.IOUtils;
import br.com.tsmweb.carros.utils.SDCardUtils;
import io.reactivex.Single;

public class DownloadServiceImpl implements DownloadService {

    private final Context context;

    public DownloadServiceImpl(@NonNull Context context) {
        this.context = context;
    }

    private final Uri onDownload(@NonNull String url) throws Exception {
        String fileName = url.substring(url.lastIndexOf("/")+1);

        try {
            File file = SDCardUtils.getPrivateFile(context, fileName, Environment.DIRECTORY_PICTURES);
            IOUtils.downloadToFile(url, file);

            return Uri.fromFile(file);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Single<List<String>> onDownload(List<Carro> carros) {
        return Single.fromCallable(
                () -> {
                    if (carros == null) throw new IllegalArgumentException("Params can't be null");

                    List<String> uris = new ArrayList<>();

                    for (Carro c : carros) {
                        // Faz download da foto do carro para o sistema de arquivo local
                        Uri uri = onDownload(c.getUrlFoto());
                        uris.add(uri.getPath());
                    }

                    return uris;
                }
        );
    }

}
