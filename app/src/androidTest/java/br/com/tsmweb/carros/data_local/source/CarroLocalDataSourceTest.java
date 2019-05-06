package br.com.tsmweb.carros.data_local.source;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.data.data_local.source.CarroLocalDataSourceImpl;
import br.com.tsmweb.carros.data.source.CarroLocalDataSource;
import br.com.tsmweb.carros.data_local.DataFactory;
import br.com.tsmweb.carros.data.data_local.dao.CarroDAO;
import br.com.tsmweb.carros.data.data_local.db.CarrosDatabase;
import br.com.tsmweb.carros.data.data_local.mapper.CarroMapper;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.utils.AppUtils;

@RunWith(AndroidJUnit4.class)
public class CarroLocalDataSourceTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private CarrosDatabase carrosDatabase;
    private CarroDAO carroDAO;
    private CarroLocalDataSource carroLocalDataSource;

    private Carro dummyCarro;
    private int tipo;
    private String tipoStr;

    @Before
    public void init() {
        carrosDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                CarrosDatabase.class
        ).build();

        carroDAO = carrosDatabase.carroDAO();
        carroLocalDataSource = new CarroLocalDataSourceImpl(carroDAO, new CarroMapper());
        dummyCarro = DataFactory.getDummyCarro();

        tipo = R.string.classicos;
        tipoStr = AppUtils.getTipoCarroByResource(tipo);
    }

    @After
    public void closeDB() {
        carrosDatabase.close();
    }

    @Test
    public void saveCarro() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertValue(it -> it == 100);
    }

    @Test
    public void deleteCarro() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertComplete();

        carroLocalDataSource.delete(dummyCarro)
                .test()
                .assertValue(it -> it > 0);
    }

    @Test
    public void deleteCarros() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertComplete();

        carroLocalDataSource.delete(Collections.singletonList(dummyCarro))
                .test()
                .assertValue(it -> it > 0);
    }

    @Test
    public void deleteCarrosByTipoComplete() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertComplete();

        carroLocalDataSource.deleteCarrosByTipo(tipoStr)
                .test()
                .assertComplete();
    }

    @Test
    public void deleteCarrosByTipoSuccess() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertComplete();

        carroLocalDataSource.deleteCarrosByTipo(tipoStr)
                .test()
                .assertValue(1);
    }

    @Test
    public void getCarrosByTipo() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertComplete();

        carroLocalDataSource.getCarrosByTipo(tipo)
                .test()
                .assertValue(it -> it != null && it.contains(dummyCarro));
    }

}
