package br.com.tsmweb.carros.ui.presentation.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.tsmweb.carros.data.data_local.mapper.CarroMapper;
import br.com.tsmweb.carros.data.repository.CarroRepositoryImpl;
import br.com.tsmweb.carros.data.source.CarroLocalDataSource;
import br.com.tsmweb.carros.data.source.CarroRemoteDataSource;
import br.com.tsmweb.carros.data.data_local.dao.CarroDAO;
import br.com.tsmweb.carros.data.data_local.db.CarrosDatabase;
import br.com.tsmweb.carros.data.data_local.source.CarroLocalDataSourceImpl;
import br.com.tsmweb.carros.data.data_remote.service.CarrosApi;
import br.com.tsmweb.carros.data.data_remote.service.CarrosService;
import br.com.tsmweb.carros.data.data_remote.source.CarroRemoteDataSourceImpl;
import br.com.tsmweb.carros.device.download.DownloadServiceImpl;
import br.com.tsmweb.carros.domain.device.download.DownloadService;
import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarro;
import br.com.tsmweb.carros.domain.interactor.carros.DeleteCarros;
import br.com.tsmweb.carros.domain.interactor.carros.GetCarros;
import br.com.tsmweb.carros.domain.interactor.carros.SaveCarro;
import br.com.tsmweb.carros.domain.interactor.carros.ShareCarros;
import br.com.tsmweb.carros.domain.interactor.carros.UpdateCarros;
import br.com.tsmweb.carros.domain.interactor.carros.ValidationCarro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import br.com.tsmweb.carros.ui.presentation.executor.UIThread;

public class CarroVmFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application application;
    private CarroDAO carroDAO;
    private CarrosService carrosService;
    private DownloadService downloadService;
    private CarroMapper localCarroMapper;
    private br.com.tsmweb.carros.data.data_remote.mapper.CarroMapper remoteCarroMapper;
    private br.com.tsmweb.carros.ui.presentation.mapper.CarroMapper presentationCarroMapper;
    private CarroLocalDataSource carroLocalDataSource;
    private CarroRemoteDataSource carroRemoteDataSource;
    private CarroRepository carroRepository;
    private UIThread uiThread;


    public CarroVmFactory(@NonNull Application application) {
        this.application = application;

        carroDAO = CarrosDatabase.getCarroDatabase(application.getApplicationContext()).carroDAO();
        carrosService = new CarrosApi().getCarrosService();

        localCarroMapper = new CarroMapper();
        remoteCarroMapper = new br.com.tsmweb.carros.data.data_remote.mapper.CarroMapper();
        presentationCarroMapper = new br.com.tsmweb.carros.ui.presentation.mapper.CarroMapper();

        carroLocalDataSource = new CarroLocalDataSourceImpl(carroDAO, localCarroMapper);
        carroRemoteDataSource = new CarroRemoteDataSourceImpl(carrosService, remoteCarroMapper);

        carroRepository = new CarroRepositoryImpl(carroLocalDataSource, carroRemoteDataSource);
        downloadService = new DownloadServiceImpl(application.getApplicationContext());

        uiThread = new UIThread();
    }

    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CarroViewModel.class)) {
            return (T) new CarroViewModel(
                    new SaveCarro(carroRepository, uiThread),
                    new DeleteCarro(carroRepository, uiThread),
                    new ValidationCarro(uiThread),
                    presentationCarroMapper
            );
        }

        if (modelClass.isAssignableFrom(CarrosViewModel.class)) {
            return (T) new CarrosViewModel(
                    application,
                    new GetCarros(carroRepository, uiThread),
                    new UpdateCarros(carroRepository, uiThread),
                    new DeleteCarros(carroRepository, uiThread),
                    new ShareCarros(downloadService, uiThread),
                    presentationCarroMapper
            );
        }

        throw new IllegalArgumentException("ViewModel Not Found");
    }

}
