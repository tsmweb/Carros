package br.com.tsmweb.carros.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.activity.CarroActivity;
import br.com.tsmweb.carros.databinding.FragmentCarrosBinding;
import br.com.tsmweb.carros.viewModel.CarrosViewModal;

public class CarrosFragment extends BaseFragment {

    private CarrosViewModal carrosViewModal;
    private FragmentCarrosBinding binding;

    private int tipo;
    protected RecyclerView recyclerView;

    // Método para instanciar esse fragment pelo tipo
    public static CarrosFragment newInstance(int tipo) {
        Bundle args = new Bundle();
        args.putInt("tipo", tipo);

        CarrosFragment f = new CarrosFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Lê o tipo dos argumentos
            this.tipo = getArguments().getInt("tipo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_carros, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        carrosViewModal = ViewModelProviders.of(this).get(CarrosViewModal.class);

        if (savedInstanceState == null) {
            carrosViewModal.init();
        }

        binding.setViewModel(carrosViewModal);

        carrosViewModal.loadCarros(tipo);

        carrosViewModal.getSelected().observe(this, carro -> {
            Intent intent = new Intent(getContext(), CarroActivity.class);
            intent.putExtra("carro", carro);
            startActivity(intent);
        });
    }

}
