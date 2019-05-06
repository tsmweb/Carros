package br.com.tsmweb.carros.ui.view.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import br.com.tsmweb.carros.R;

public class DeletarCarroDialog extends DialogFragment {

    private static final String TAG = DeletarCarroDialog.class.getSimpleName();

    private Callback callback;

    public interface Callback {
        void onClickYes();
    }

    public static void show(FragmentManager fm, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("deletar_carro");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        DeletarCarroDialog frag = new DeletarCarroDialog();
        frag.callback = callback;
        frag.show(ft, "deletar_carro");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (callback != null) {
                        // Confirmou que vai deletar o carro
                        callback.onClickYes();
                    }

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.deletar_esse_carro);
        builder.setPositiveButton(R.string.sim, dialogClickListener);
        builder.setNegativeButton(R.string.nao, dialogClickListener);

        return builder.create();
    }
}
