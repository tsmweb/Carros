package br.com.tsmweb.carros.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.FragmentCarroBinding;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.viewModel.CarroViewModal;

public class CarroFragment extends BaseFragment {

    private Carro carro;
    private FragmentCarroBinding binding;
    private CarroViewModal carroViewModal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_carro, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        carro = getArguments().getParcelable("carro");
        carroViewModal = ViewModelProviders.of(this).get(CarroViewModal.class);
        binding.setViewModal(carroViewModal);

        carroViewModal.setCarro(carro);
    }

}
