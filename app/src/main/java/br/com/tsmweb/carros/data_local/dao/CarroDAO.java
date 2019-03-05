package br.com.tsmweb.carros.data_local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.tsmweb.carros.data_local.model.CarroEntity;
import io.reactivex.Flowable;

@Dao
public interface CarroDAO {

    @Query("SELECT * FROM carro WHERE _id = :id")
    CarroEntity getById(long id);

    @Query("SELECT * FROM carro")
    Flowable<List<CarroEntity>> getAll();

    @Query("SELECT * FROM carro WHERE tipo = :tipo")
    Flowable<List<CarroEntity>> getAllByTipo(String tipo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(CarroEntity carro);

    @Delete
    int delete(List<CarroEntity> carros);

    @Query("DELETE FROM carro WHERE tipo = :tipo")
    int deleteCarrosByTipo(String tipo);

}
