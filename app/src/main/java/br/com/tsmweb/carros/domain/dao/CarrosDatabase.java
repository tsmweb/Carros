package br.com.tsmweb.carros.domain.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.tsmweb.carros.domain.Carro;

@Database(entities = {Carro.class}, version = 1, exportSchema = false)
public abstract class CarrosDatabase extends RoomDatabase {

    private static CarrosDatabase instance;

    public abstract CarroDAO carroDAO();

    public static CarrosDatabase getCarroDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CarrosDatabase.class,
                    "carros.sqlite")
                    .build();
        }

        return instance;
    }

}
