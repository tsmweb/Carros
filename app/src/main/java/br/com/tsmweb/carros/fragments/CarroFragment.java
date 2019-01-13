package br.com.tsmweb.carros.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import br.com.tsmweb.carros.CarrosApplication;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.FragmentCarroBinding;
import br.com.tsmweb.carros.fragments.dialog.DeletarCarroDialog;
import br.com.tsmweb.carros.fragments.dialog.EditarCarroDialog;
import br.com.tsmweb.carros.viewModel.CarroViewModal;

public class CarroFragment extends BaseFragment {

    private FragmentCarroBinding binding;
    private CarroViewModal carroViewModal;

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

        carroViewModal = ViewModelProviders.of(getActivity()).get(CarroViewModal.class);
        binding.setViewModal(carroViewModal);
        binding.executePendingBindings();

        startViewModalObservable();
    }

    private void startViewModalObservable() {
        carroViewModal.getUpdated().observe(this, isUpdated -> {
            if (isUpdated) {
                toast(R.string.carro_atualizado);
                // Envia o evento para o bus
                CarrosApplication.getInstance().getBus().post("refresh");
            }
        });

        carroViewModal.getDeleted().observe(this, isDeleted -> {
            if (isDeleted) {
                // Envia o evento para o bus
                CarrosApplication.getInstance().getBus().post("refresh");

                // Fecha a activity
                getActivity().finish();
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
                carroViewModal.onCarroDelete();
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
