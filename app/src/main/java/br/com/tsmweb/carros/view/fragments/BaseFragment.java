package br.com.tsmweb.carros.view.fragments;

import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BaseFragment extends DebugFragment {

    public Context getContext() {
        return getActivity();
    }

    protected void setTextString(int resId, String text) {
        View view = getView();

        if (view != null) {
            TextView t = view.findViewById(resId);

            if (t != null) {
                t.setText(text);
            }
        }
    }

    protected String getTextString(int resId) {
        View view = getView();

        if (view != null) {
            TextView t = view.findViewById(resId);

            if (t != null) {
                return t.getText().toString();
            }
        }
        return null;
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void toast(int msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void alert(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void snack(View view, int msg, final Runnable runnable) {
        snack(view, getString(msg), runnable);
    }

    protected void snack(View view, int msg) {
        snack(view, getString(msg), null);
    }

    protected void snack(View view, String msg) {
        snack(view, msg, null);
    }

    protected void snack(View view, String msg, final Runnable runnable) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Ok", v -> {
                    if (runnable != null) {
                        runnable.run();
                    }
                }).show();
    }

    protected void alert(int msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void log(String msg) {
        Log.d(TAG, msg);
    }

    public androidx.appcompat.app.ActionBar getActionBar() {
        AppCompatActivity ac = getAppCompatActivity();
        return ac.getSupportActionBar();
    }

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    public boolean getBoolean(int res) {
        return getActivity().getResources().getBoolean(res);
    }

}
