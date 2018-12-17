package br.com.tsmweb.carros.activity;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.utils.AlertUtils;

public class BaseActivity extends DebugActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected Context getContext() {
        return this;
    }

    protected Activity getActivity() {
        return this;
    }

    protected void log(String msg) {
        Log.d(TAG, msg);
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void alert(String msg) {
        AlertUtils.alert(this, msg);
    }

    protected void alert(String title, String msg) {
        AlertUtils.alert(this, title, msg);
    }

    protected void alert(int msg) {
        AlertUtils.alert(this, getString(msg));
    }

    protected void alert(int title, int msg) {
        AlertUtils.alert(this,getString(title), getString(msg));
    }

    protected void snack(View view, int msg, Runnable runnable) {
        this.snack(view, this.getString(msg), runnable);
    }

    protected void snack(View view, int msg) {
        this.snack(view, this.getString(msg), (Runnable) null);
    }

    protected void snack(View view, String msg) {
        this.snack(view, msg, (Runnable) null);
    }

    protected void snack(View view, String msg, final Runnable runnable) {
        Snackbar.make(view, msg, 0).setAction("Ok", v -> {
            if (runnable != null) {
                runnable.run();
            }
        }).show();
    }

    public boolean getBoolean(int res) {
        return getResources().getBoolean(res);
    }

    protected Toolbar setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        return toolbar;
    }

    protected void replaceFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, "TAG").commit();
    }

}
