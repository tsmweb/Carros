package br.com.tsmweb.carros.view.fragments;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.FragmentCarroBinding;
import br.com.tsmweb.carros.view.fragments.dialog.DeletarCarroDialog;
import br.com.tsmweb.carros.view.fragments.dialog.EditarCarroDialog;
import br.com.tsmweb.carros.presentation.viewModel.CarroViewModel;

public class CarroFragment extends BaseFragment {

    private FragmentCarroBinding binding;
    private CarroViewModel carroViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_carro, container, false);
        View view = binding.getRoot();
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Cria o viewModel
        carroViewModel = ViewModelProviders.of(getActivity()).get(CarroViewModel.class);
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

        carroViewModel.getUpdateState().observe(getViewLifecycleOwner(), state -> {
            switch (state.status) {
                case SUCCESS:
                    toast(R.string.carro_atualizado);
                    break;
                case ERROR:
                    toast(state.error.getMessage());
                    break;
            }
        });

        carroViewModel.getDeleteState().observe(getViewLifecycleOwner(), state -> {
            switch (state.status) {
                case SUCCESS:
                    // Fecha a activity
                    getActivity().finish();
                    break;
                case ERROR:
                    toast(R.string.msg_error_update_carro);
                    break;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_carro, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            EditarCarroDialog.show(getFragmentManager());

            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            DeletarCarroDialog.show(getFragmentManager(), () -> {
                toast(R.string.carro_deletado);
                carroViewModel.onCarroDelete();
            });

            return true;
        } else if (item.getItemId() == R.id.action_share) {
            toast("Compartilhar");
        } else if (item.getItemId() == R.id.action_maps) {
            toast("Mapa");
        } else if (item.getItemId() == R.id.action_video) {
            toast("Vídeo");
        }

        return super.onOptionsItemSelected(item);
    }

}
