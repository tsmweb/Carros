package br.com.tsmweb.carros.data_local.source;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.UUID;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.data.source.CarroLocalDataSource;
import br.com.tsmweb.carros.data_local.dao.CarroDAO;
import br.com.tsmweb.carros.data_local.db.CarrosDatabase;
import br.com.tsmweb.carros.data_local.mapper.CarroMapper;
import br.com.tsmweb.carros.domain.model.Carro;

@RunWith(AndroidJUnit4.class)
public class CarroLocalDataSourceTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private CarrosDatabase carrosDatabase;
    private CarroDAO carroDAO;
    private CarroLocalDataSource carroLocalDataSource;

    private Carro dummyCarro;

    @Before
    public void init() {
        carrosDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                CarrosDatabase.class
        ).build();

        carroDAO = carrosDatabase.carroDAO();
        carroLocalDataSource = new CarroLocalDataSourceImpl(carroDAO, new CarroMapper());

        dummyCarro = new Carro();
        dummyCarro.setId(100);
        dummyCarro.setTipo("classicos");
        dummyCarro.setNome("Camaro");
        dummyCarro.setDesc(UUID.randomUUID().toString() + " - Camaro");
        dummyCarro.setUrlFoto(UUID.randomUUID().toString());
        dummyCarro.setUrlVideo(UUID.randomUUID().toString());
        dummyCarro.setUrlInfo(UUID.randomUUID().toString());
        dummyCarro.setLatitude(UUID.randomUUID().toString());
        dummyCarro.setLongitude(UUID.randomUUID().toString());
    }

    @After
    public void closeDB() {
        carrosDatabase.close();
    }

    @Test
    public void saveCarro() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertValue(id -> id == 100);
    }

    @Test
    public void deleteCarro() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertComplete();

        carroLocalDataSource.delete(dummyCarro)
                .test()
                .assertValue(v -> v > 0);
    }

    @Test
    public void deleteCarros() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertComplete();

        carroLocalDataSource.delete(Collections.singletonList(dummyCarro))
                .test()
                .assertValue(v -> v > 0);
    }

    @Test
    public void getCarrosByTipo() {
        carroLocalDataSource.save(dummyCarro)
                .test()
                .assertComplete();

        carroLocalDataSource.getCarrosByTipo(R.string.classicos)
                .test()
                .assertValue(listCarros -> listCarros != null && listCarros.contains(dummyCarro));
    }

}
