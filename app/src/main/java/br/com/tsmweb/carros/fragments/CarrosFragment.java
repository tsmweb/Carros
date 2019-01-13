package br.com.tsmweb.carros.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import br.com.tsmweb.carros.CarrosApplication;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.activity.CarroActivity;
import br.com.tsmweb.carros.databinding.FragmentCarrosBinding;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.utils.AlertUtils;
import br.com.tsmweb.carros.utils.AndroidUtils;
import br.com.tsmweb.carros.viewModel.CarrosViewModal;

public class CarrosFragment extends BaseFragment {

    private static final String TAG = CarrosFragment.class.getSimpleName();

    private CarrosViewModal carrosViewModal;
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

        // Registra a classe para receber eventos
        CarrosApplication.getInstance().getBus().register(this);
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
        getLifecycle().addObserver(carrosViewModal);
        binding.setViewModel(carrosViewModal);
        binding.executePendingBindings();

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
        carrosViewModal.getLoading().observe(this, isLoading -> {
            if (carrosViewModal.getPullToRefresh()) {
                if (!isLoading) {
                    swipeLayout.setRefreshing(false);
                    carrosViewModal.setPullToRefresh(false);
                } else {
                    swipeLayout.setRefreshing(true);
                }
            } else {
                if (isLoading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Observa e recebe o carro selecionado pelo evento Click
        carrosViewModal.getSelected().observe(this, carro -> {
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
        carrosViewModal.getSelectedLong().observe(this, carro -> {
            setupActionMode();
        });

        // Observa quando um ou mais carros forem deletados
        carrosViewModal.getDeleted().observe(this, isDeleted -> {
            if (isDeleted) {
                snack(recyclerView, R.string.carros_excluidos_sucesso);
            }
        });

        // Observa o compartilhamento dos carros delecionados
        carrosViewModal.getImageUris().observe(this, uris -> {
            shareSelectedCarros(uris);
        });

        // Observa o download das imagens dos carros selecionados
        carrosViewModal.getDownloadingImage().observe(this, isDownload -> {
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
        if (actionMode != null || carrosViewModal.getCountSelectedCarro() == 0) { return; }

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

        int countSelected = carrosViewModal.getCountSelectedCarro();

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
                    carrosViewModal.deleteSelectedCarros();
                } else if (item.getItemId() == R.id.action_share) {
                    // Valida de existe conexão com internet
                    if (AndroidUtils.isNetworkAvailable(getContext())) {
                        carrosViewModal.shareSelectedCarros();
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
                carrosViewModal.deselectCarros();

                if (recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Cancela o recebimento de eventos
        CarrosApplication.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onBusAtualizarListaCarros(String refresh) {
        // Recebeu o evento, atualiza a lista
        carrosViewModal.loadCarros(tipo);
    }
}
