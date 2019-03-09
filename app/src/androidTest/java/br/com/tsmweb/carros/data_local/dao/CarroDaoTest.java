package br.com.tsmweb.carros.data_local.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import br.com.tsmweb.carros.data_local.DataFactory;
import br.com.tsmweb.carros.data_local.db.CarrosDatabase;
import br.com.tsmweb.carros.data_local.model.CarroEntity;

@RunWith(AndroidJUnit4.class)
public class CarroDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private CarrosDatabase carrosDatabase;
    private CarroDAO carroDAO;

    private CarroEntity dummyCarro;

    @Before
    public void initDB() {
        carrosDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                CarrosDatabase.class
        ).build();

        carroDAO = carrosDatabase.carroDAO();
        dummyCarro = DataFactory.getDummyCarroEntity();
    }

    @After
    public void closeDB() {
        carrosDatabase.close();
    }

    @Test
    public void saveCarro() {
        long id = carroDAO.save(dummyCarro);

        Assert.assertEquals(100, id);
    }

    @Test
    public void getCarroById() {
        carroDAO.save(dummyCarro);
        CarroEntity carroEntity = carroDAO.getById(100);

        Assert.assertNotNull(carroEntity);
        Assert.assertEquals(100, carroEntity.getId());
        Assert.assertEquals("classicos", carroEntity.getTipo());
    }

    @Test
    public void getCarrosByTipo() {
        carroDAO.save(dummyCarro);
        carroDAO.getAllByTipo("classicos")
                .test()
                .assertValue(it -> it != null && it.contains(dummyCarro));
    }

    @Test
    public void getCarrosAll() {
        carroDAO.save(dummyCarro);
        carroDAO.getAll()
                .test()
                .assertValue(it -> it != null && it.contains(dummyCarro));
    }

    @Test
    public void deleteCarro() {
        carroDAO.save(dummyCarro);
        int i = carroDAO.delete(Collections.singletonList(dummyCarro));

        Assert.assertEquals(1, i);
    }

    @Test
    public void deleteCarrosByTipo() {
        carroDAO.save(dummyCarro);
        int i = carroDAO.deleteCarrosByTipo("classicos");

        Assert.assertEquals(1, i);
    }

}
