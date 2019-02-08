package br.com.tsmweb.carros.view.fragments.dialog;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.DialogEditarCarroBinding;
import br.com.tsmweb.carros.view.viewModel.CarroViewModel;

public class EditarCarroDialog extends DialogFragment {

    private static final String TAG = EditarCarroDialog.class.getSimpleName();

    private DialogEditarCarroBinding binding;
    private CarroViewModel carroViewModel;

    // Método utilitário para criar o dialog
    public static void show(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("editar_carro");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        EditarCarroDialog frag = new EditarCarroDialog();
        frag.show(ft, "editar_carro");
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) {
            return;
        }

        // Atualiza o tamanho do dialog
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_editar_carro, container, false);
        View rootView = binding.getRoot();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Cria o viewModel
        carroViewModel = ViewModelProviders.of(getActivity()).get(CarroViewModel.class);
        binding.setViewModal(carroViewModel);

        startViewModalObservable();
    }

    private void startViewModalObservable() {
        carroViewModel.getState().observe(getViewLifecycleOwner(), state -> {
            switch (state.status) {
                case UPDATE:
                    if (state.data) {
                        // Fecha o DialogFragment
                        dismiss();
                    }

                    break;
                case ERROR:
                    if (state.data) {
                        binding.edtNome.setError(getString(R.string.informe_nome));
                    }

                    break;
            }
        });
    }

}
