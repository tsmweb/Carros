package br.com.tsmweb.carros.data.dao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import br.com.tsmweb.carros.data.Carro;

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