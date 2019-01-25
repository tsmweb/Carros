package br.com.tsmweb.carros.domain.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.com.tsmweb.carros.domain.Carro;
import io.reactivex.Flowable;

@Dao
public interface CarroDAO {

    @Query("SELECT * FROM carro WHERE _id = :id")
    Carro getById(long id);

    @Query("SELECT * FROM carro")
    Flowable<List<Carro>> getAll();

    @Query("SELECT * FROM carro WHERE tipo = :tipo")
    Flowable<List<Carro>> getAllByTipo(String tipo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Carro carro);

    @Delete
    int delete(List<Carro> carros);

    @Query("DELETE FROM carro WHERE tipo = :tipo")
    int deleteCarrosByTipo(String tipo);

}
