package br.com.tsmweb.carros.data;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import br.com.tsmweb.carros.DataFactory;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.data.repository.CarroRepositoryImpl;
import br.com.tsmweb.carros.data.source.CarroLocalDataSource;
import br.com.tsmweb.carros.data.source.CarroRemoteDataSource;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Flowable;
import io.reactivex.Single;

@RunWith(JUnit4.class)
public class CarroRepositoryTest {

    private CarroRepository carroRepository;

    @Mock
    private CarroLocalDataSource carroLocalDataSource;

    @Mock
    private CarroRemoteDataSource carroRemoteDataSource;

    private Carro dummyCarro;
    private List<Carro> dummyCarros;
    private  int tipo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        carroRepository = new CarroRepositoryImpl(carroLocalDataSource, carroRemoteDataSource);
        dummyCarro = DataFactory.makeDummyCarro(1);
        dummyCarros = DataFactory.makeDummyListCarro(3);
        tipo = R.string.classicos;
    }

    @Test
    public void saveCarroCompletes() {
        stubSaveCarro_CarroLocalDataSource(Single.just(1L));

        carroRepository.save(dummyCarro)
                .test()
                .assertComplete();
    }

    @Test
    public void saveCarroSuccess() {
        stubSaveCarro_CarroLocalDataSource(Single.just(1L));

        carroRepository.save(dummyCarro)
                .test()
                .assertValue(it -> it == 1);
    }

    @Test
    public void saveCarroErrors() {
        Throwable err = new Throwable("Test Error");
        stubSaveCarro_CarroLocalDataSource(Single.error(err));

        carroRepository.save(dummyCarro)
                .test()
                .assertError(err);
    }

    @Test
    public void deleteCarroCompletes() {
        stubDeleteCarro_CarroLocalDataSource(Single.just(1));

        carroRepository.delete(dummyCarro)
                .test()
                .assertComplete();
    }

    @Test
    public void deleteCarroSuccess() {
        stubDeleteCarro_CarroLocalDataSource(Single.just(1));

        carroRepository.delete(dummyCarro)
                .test()
                .assertValue(it -> it == 1);
    }

    @Test
    public void deleteCarroErros() {
        Throwable err = new Throwable("Test Error");
        stubDeleteCarro_CarroLocalDataSource(Single.error(err));

        carroRepository.delete(dummyCarro)
                .test()
                .assertError(err);
    }

    @Test
    public void deleteCarrosCompletes() {
        stubDeleteCarros_CarroLocalDataSource(Single.just(dummyCarros.size()));

        carroRepository.delete(dummyCarros)
                .test()
                .assertComplete();
    }

    @Test
    public void deleteCarrosSuccess() {
        stubDeleteCarros_CarroLocalDataSource(Single.just(dummyCarros.size()));

        carroRepository.delete(dummyCarros)
                .test()
                .assertValue(it -> it == dummyCarros.size());
    }

    @Test
    public void deleteCarrosErrors() {
        Throwable err = new Throwable("Test Error");
        stubDeleteCarros_CarroLocalDataSource(Single.error(err));

        carroRepository.delete(dummyCarros)
                .test()
                .assertError(err);
    }

    @Test
    public void getCarrosByTipoCompletes() {
        stubGetCarrosByTipo_CarroLocalDataSource(Flowable.just(dummyCarros));

        carroRepository.getCarrosByTipo(R.string.classicos)
                .test()
                .assertComplete();
    }

    @Test
    public void getCarrosByTipoReturnsData() {
        stubGetCarrosByTipo_CarroLocalDataSource(Flowable.just(dummyCarros));

        carroRepository.getCarrosByTipo(R.string.classicos)
                .test()
                .assertValue(dummyCarros);
    }

    @Test
    public void getCarrosByTipoErrors() {
        Throwable err = new Throwable("Test Error");
        stubGetCarrosByTipo_CarroLocalDataSource(Flowable.error(err));

        carroRepository.getCarrosByTipo(R.string.classicos)
                .test()
                .assertError(err);
    }

    @Test
    public void updateCarrosComplete() {
        stubGetCarros_CarroRemoteDataSource(Single.just(dummyCarros));
        stubSaveCarro_CarroLocalDataSource(Single.just(1L));

        carroRepository.updateCarros(tipo)
                .test()
                .assertComplete();
    }

    @Test
    public void updateCarrosRemoteErrors() {
        Throwable err = new Throwable("Test Error");
        stubGetCarros_CarroRemoteDataSource(Single.error(err));
        stubSaveCarro_CarroLocalDataSource(Single.just(1L));

        carroRepository.updateCarros(tipo)
                .test()
                .assertError(err);
    }

    @Test
    public void updateCarrosLocalErrors() {
        Throwable err = new Throwable("Test Error");
        stubGetCarros_CarroRemoteDataSource(Single.just(dummyCarros));
        stubSaveCarro_CarroLocalDataSource(Single.error(err));

        carroRepository.updateCarros(tipo)
                .test()
                .assertError(err);
    }

    private void stubSaveCarro_CarroLocalDataSource(Single single) {
        when(carroLocalDataSource.save(any(Carro.class))).thenReturn(single);
    }

    private void stubDeleteCarro_CarroLocalDataSource(Single single) {
        when(carroLocalDataSource.delete(any(Carro.class))).thenReturn(single);
    }

    private void stubDeleteCarros_CarroLocalDataSource(Single single) {
        when(carroLocalDataSource.delete(anyList())).thenReturn(single);
    }

    private void stubGetCarrosByTipo_CarroLocalDataSource(Flowable flowable) {
        when(carroLocalDataSource.getCarrosByTipo(tipo)).thenReturn(flowable);
    }

    private void stubGetCarros_CarroRemoteDataSource(Single single) {
        when(carroRemoteDataSource.getCarros(tipo)).thenReturn(single);
    }

}
