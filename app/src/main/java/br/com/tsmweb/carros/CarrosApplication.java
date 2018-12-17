package br.com.tsmweb.carros;

import android.app.Application;
import android.util.Log;

public class CarrosApplication extends Application {

    private static final String TAG = "CarrosApplication";

    private static CarrosApplication instance = null;

    public static CarrosApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "CarrosApplication.onCreate()");
        // Salva a instância para termos acesso como singleton
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Log.d(TAG, "CarrosApplication.onTerminate()");
    }
}
