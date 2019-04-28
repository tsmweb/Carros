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
import android.widget.Toast;

import br.com.tsmweb.carros.CarrosApplication;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.DialogEditarCarroBinding;
import br.com.tsmweb.carros.presentation.viewModel.CarroViewModel;
import br.com.tsmweb.carros.presentation.viewModel.CarroVmFactory;

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
        carroViewModel = ViewModelProviders.of(getActivity(), new CarroVmFactory(CarrosApplication.getInstance())).get(CarroViewModel.class);

        // Dispara o método responsavel por atualizar o carro no ViewModel
        binding.btnAtualizar.setOnClickListener(v -> carroViewModel.onCarroUpdate());

        subscriberViewModalObservable();
    }

    private void subscriberViewModalObservable() {
        carroViewModel.getLoadState().observe(getViewLifecycleOwner(), state -> {
            switch (state.status) {
                case SUCCESS:
                    binding.setCarro(state.data);
                    break;
            }
        });

        carroViewModel.getValidationState().observe(getViewLifecycleOwner(), state -> {
            switch (state.status) {
                case INVALID:
                    handleValidation(state.data);
                    break;
                case ERROR:
                    Toast.makeText(getContext(), R.string.msg_error_valida_dados, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        carroViewModel.getUpdateState().observe(getViewLifecycleOwner(), state -> {
            switch (state.status) {
                case SUCCESS:
                case ERROR:
                    // Fecha o DialogFragment
                    dismiss();
                    break;
            }
        });
    }

    private void handleValidation(String field) {
        if (field.equals("nome")) {
            binding.edtNome.setError(getString(R.string.informe_nome));
        }
    }

}
