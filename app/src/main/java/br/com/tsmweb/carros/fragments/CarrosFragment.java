package br.com.tsmweb.carros.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.activity.CarroActivity;
import br.com.tsmweb.carros.databinding.FragmentCarrosBinding;
import br.com.tsmweb.carros.utils.AndroidUtils;
import br.com.tsmweb.carros.viewModel.CarrosViewModal;

public class CarrosFragment extends BaseFragment {

    private CarrosViewModal carrosViewModal;
    private FragmentCarrosBinding binding;

    private int tipo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeLayout;

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

        progressBar = view.findViewById(R.id.progress);

        // Swipe to Refresh
        swipeLayout = view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

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
        startViewModalObservable();

        if (savedInstanceState == null) {
            carrosViewModal.loadCarros(tipo);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return () -> {
            // Atualiza ao fazer o gesto Pull to Refresh
            // Valida se existe conexão ao fazer o gesto Pull to Refresh
            if (AndroidUtils.isNetworkAvailable(getContext())) {
                carrosViewModal.setPullToRefresh(true);
                carrosViewModal.loadCarros(tipo);
            } else {
                swipeLayout.setRefreshing(false);
                snack(recyclerView, R.string.msg_error_conexao_indisponivel);
            }
        };
    }

    private void startViewModalObservable() {
        // Observa o carregamento das informações do web service
        carrosViewModal.getLoading().observe(this, loading -> {
            if (carrosViewModal.getPullToRefresh()) {
                if (!loading) {
                    swipeLayout.setRefreshing(false);
                    carrosViewModal.setPullToRefresh(false);
                } else {
                    swipeLayout.setRefreshing(true);
                }
            } else {
                if (loading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Observa e recebe o carro selecionado
        carrosViewModal.getSelected().observe(this, carro -> {
            Intent intent = new Intent(getContext(), CarroActivity.class);
            intent.putExtra("carro", carro);
            startActivity(intent);
        });
    }

}
