package br.com.tsmweb.carros.view.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.view.activity.CarroActivity;
import br.com.tsmweb.carros.databinding.FragmentCarrosBinding;
import br.com.tsmweb.carros.data.Carro;
import br.com.tsmweb.carros.utils.AlertUtils;
import br.com.tsmweb.carros.utils.AndroidUtils;
import br.com.tsmweb.carros.view.viewModel.CarrosViewModel;

public class CarrosFragment extends BaseFragment {

    private static final String TAG = CarrosFragment.class.getSimpleName();

    private CarrosViewModel carrosViewModel;
    private FragmentCarrosBinding binding;

    private int tipo;
    private ActionMode actionMode;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeLayout;
    private AlertDialog alertProgress;

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

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        progressBar = view.findViewById(R.id.progress);

        // Swipe to Refresh
        swipeLayout = binding.swipeToRefresh;
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

        // Cria o viewModel
        carrosViewModel = ViewModelProviders.of(this).get(CarrosViewModel.class);

        getLifecycle().addObserver(carrosViewModel);
        binding.setViewModel(carrosViewModel);

        startViewModalObservable();

        if (savedInstanceState == null) {
            carrosViewModel.loadCarros(tipo);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return () -> {
            // Atualiza ao fazer o gesto Pull to Refresh
            // Valida se existe conexão ao fazer o gesto Pull to Refresh
            if (AndroidUtils.isNetworkAvailable(getContext())) {
                carrosViewModel.downloadCarros(tipo);
            } else {
                swipeLayout.setRefreshing(false);
                snack(recyclerView, R.string.msg_error_conexao_indisponivel);
            }
        };
    }

    private void startViewModalObservable() {
        carrosViewModel.getState().observe(getViewLifecycleOwner(), state -> {
            switch (state.status) {
                case LOADING: // Observa o carregamento das informações da base de dados
                    if (state.data) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    break;
                case ERROR: // Observa o erro gerado nas operações com os carros
                    if (state.data) {
                        snack(recyclerView, "Falha na operação!");
                    }

                    break;
                case DELETE:
                    if (state.data) { // Observa quando um ou mais carros forem deletados
                        snack(recyclerView, R.string.carros_excluidos_sucesso);
                    }
            }
        });

        // Observa o carregamento das informações do web service
        carrosViewModel.getPullToRefresh().observe(getViewLifecycleOwner(), isRefreshing -> {
            swipeLayout.setRefreshing(isRefreshing);
        });

        // Observa e recebe o carro selecionado pelo evento Click
        carrosViewModel.getSelected().observe(getViewLifecycleOwner(), carro -> {
            if (actionMode == null) {
                openDetailsCarro(carro);
            } else { // Se a CAB está ativada
                // Atualiza o título com a quantidade de carros selecionados
                updateActionModeTitle();

                // Redesenha a lista
                if (recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        // Observa e recebe o carro selecionado pelo evento LongClick
        carrosViewModel.getSelectedLong().observe(getViewLifecycleOwner(), carro -> {
            if (carro != null) {
                setupActionMode();
            }
        });

        // Observa o compartilhamento dos carros delecionados
        carrosViewModel.getImageUris().observe(getViewLifecycleOwner(), uris -> {
            shareSelectedCarros(uris);
        });

        // Observa o download das imagens dos carros selecionados
        carrosViewModel.getDownloadingImage().observe(getViewLifecycleOwner(), isDownload -> {
            if (isDownload) {
                alertProgress = AlertUtils.progress(getContext(), getString(R.string.aguarde), getString(R.string.preparando_carros));
                alertProgress.show();
            } else {
                if (alertProgress != null) {
                    alertProgress.dismiss();
                }
            }
        });

    }

    // Abre tela de detalhes do carro selecionado
    private void openDetailsCarro(Carro carro) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("carro", carro);

        Intent intent = new Intent(getContext(), CarroActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    // Configura a Action Bar de Contexto (CAB)
    private void setupActionMode() {
        if (actionMode != null || carrosViewModel.getCountSelectedCarro() == 0) { return; }

        // Liga a action bar de contexto (CAB)
        actionMode = getAppCompatActivity().startSupportActionMode(getActionModeCallback());

        // Solicita ao Android para desenhar a lista novamente
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

        // Atualiza o título para mostrar a quantidade de carros selecionados.
        updateActionModeTitle();
    }

    // Atualiza o título da action bar (CAB)
    private void updateActionModeTitle() {
        if (actionMode != null) {
            actionMode.setTitle(R.string.selecione_carros);
            actionMode.setSubtitle(null);
        }

        int countSelected = carrosViewModel.getCountSelectedCarro();

        if (countSelected == 1) {
            actionMode.setSubtitle(R.string.carro_selecionado);
        } else if (countSelected > 1) {
            actionMode.setSubtitle(getString(R.string.carros_selecionados, countSelected));
        }
    }

    // Compartilha os carros selecionados
    private void shareSelectedCarros(ArrayList<Uri> uris) {
        // Cria a intent com a foto dos carros
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        shareIntent.setType("image/*");

        // Cria o Intent Chooser com as opções
        startActivity(Intent.createChooser(shareIntent, getString(R.string.enviar_carros)));
    }

    private ActionMode.Callback getActionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                // Infla o menu específico da action bar de contexto (CAB)
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.menu_frag_carros_cab, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
                if (item.getItemId() == R.id.action_remove) {
                    carrosViewModel.deleteSelectedCarros();
                } else if (item.getItemId() == R.id.action_share) {
                    // Valida de existe conexão com internet
                    if (AndroidUtils.isNetworkAvailable(getContext())) {
                        carrosViewModel.shareSelectedCarros();
                    } else {
                        snack(recyclerView, R.string.msg_error_conexao_indisponivel);
                    }
                }

                // Encerra o action mode
                actionMode.finish();

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Limpa o estado
                actionMode = null;

                // Configura todos os carros para não selecionados
                carrosViewModel.deselectCarros();

                if (recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getLifecycle().removeObserver(carrosViewModel);
    }

}
