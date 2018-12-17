package br.com.tsmweb.carros.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AlertUtils {

    public static void alert(Activity activity, String title, String message) {
        alert(activity, title, message, 0, 0);
    }

    public static void alert(Activity activity, String message) {
        alert(activity, null, message, 0, 0);
    }

    public static void alert(Activity activity, String title, String message, int okButton, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (icon > 0) {
            builder.setIcon(icon);
        }

        if (title != null) {
            builder.setTitle(title);
        }

        builder.setMessage(message);

        String okString = okButton > 0 ? activity.getString(okButton) : "OK";

        AlertDialog alertDialog = builder.create();
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, okString, (dialog, which) -> {
            return;
        });

        alertDialog.show();
    }

    public static void alert(Context context, int title, int message, int okButton, final Runnable runnable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message);
        String okString = okButton > 0 ? context.getString(okButton) : "OK";
        // Add the buttons
        builder.setPositiveButton(okString, (dialog, id) -> {
            if (runnable != null) {
                runnable.run();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
