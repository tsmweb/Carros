package br.com.tsmweb.carros.ui.view.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.utils.AndroidUtils;

public class AboutDialog extends DialogFragment {

    // Método utilitário para mostrar o dialog
    public static void showAbout(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_about");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        new AboutDialog().show(ft, "dialog_about");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Cria o HTML com o texto de sobre do aplicativo
        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        // Versão do aplicativo
        String versionName = AndroidUtils.getVersionName(getActivity());
        // Converte o texto do strings.xml para HTML
        aboutBody.append(Html.fromHtml(getString(R.string.about_dialog_text, versionName)));

        // Infla o layout
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        TextView view = (TextView) inflater.inflate(R.layout.dialog_about, null);
        view.setText(aboutBody);
        view.setMovementMethod(new LinkMovementMethod());

        // Cria o dialog customizado
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about_dialog_title)
                .setView(view)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                }).create();
    }
}
