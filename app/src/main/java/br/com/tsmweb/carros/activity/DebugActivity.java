package br.com.tsmweb.carros.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DebugActivity extends AppCompatActivity {

    protected final String TAG = this.getClassName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLogLifecycle()) {
            log(getClassName() + ".onCreate(): " + savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isLogLifecycle()) {
            log(getClassName() + ".onStart().");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (isLogLifecycle()) {
            log(getClassName() + ".onRestart().");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isLogLifecycle()) {
            log(getClassName() + ".onResume().");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (isLogLifecycle()) {
            log(getClassName() + ".onSaveInstanceState().");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isLogLifecycle()) {
            log(getClassName() + ".onPause().");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isLogLifecycle()) {
            log(getClassName() + ".onStop().");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isLogLifecycle()) {
            log(getClassName() + ".onDestroy().");
        }
    }

    public String getClassName() {
        // Retorna o nome da classe sem o pacote
        Class cls = ((Object) this).getClass();
        String s = cls.getSimpleName();

        return s;
    }

    protected void log(String msg) {
        if (isLogOn()) {
            Log.d(TAG, msg);
        }
    }

    protected boolean isLogOn() {
        return true;
    }

    protected boolean isLogLifecycle() {
        return true;
    }

}
