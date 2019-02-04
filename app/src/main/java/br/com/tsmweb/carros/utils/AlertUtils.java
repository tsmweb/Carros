package br.com.tsmweb.carros.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.app.AlertDialog;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

    public static AlertDialog progress(Context context, String title, String message) {
        final ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        progressBar.setIndeterminate(true);

        final LinearLayout container = new LinearLayout(context);
        container.addView(progressBar);

        int padding = getDialogPadding(context);

        container.setPadding(padding, (message == null ? padding : 0), padding, 0);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setView(container);

        return builder.create();
    }

    private static int getDialogPadding(Context context) {
        int[] sizeAttr = new int[] { androidx.appcompat.R.attr.dialogPreferredPadding };
        TypedArray a = context.obtainStyledAttributes((new TypedValue()).data, sizeAttr);
        int size = a.getDimensionPixelSize(0, -1);
        a.recycle();

        return size;
    }

}
