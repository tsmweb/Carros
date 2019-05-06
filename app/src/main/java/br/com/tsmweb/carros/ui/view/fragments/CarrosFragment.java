package br.com.tsmweb.carros.ui.view.fragments;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.CarrosApplication;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.utils.AlertUtils;
import br.com.tsmweb.carros.ui.presentation.model.CarroBinding;
import br.com.tsmweb.carros.ui.presentation.viewModel.CarroVmFactory;
import br.com.tsmweb.carros.ui.presentation.viewModel.ViewState;
import br.com.tsmweb.carros.ui.view.adapter.CarroAdapter;
import br.com.tsmweb.carros.ui.view.activity.CarroActivity;
import br.com.tsmweb.carros.databinding.FragmentCarrosBinding;
import br.com.tsmweb.carros.utils.AndroidUtils;
import br.com.tsmweb.carros.ui.presentation.viewModel.CarrosViewModel;

public class CarrosFragment extends BaseFragment {

    private static final String TAG = CarrosFragment.class.getSimpleName();

    private CarrosViewModel carrosViewModel;
    private FragmentCarrosBinding binding;

    private CarroAdapter carroAdapter;

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

        // Cria o adapter e configura o recyclerView
        carroAdapter = new CarroAdapter(getContext(), onClickCarro(), onLongClickCarro());

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(carroAdapter);

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
        carrosViewModel = ViewModelProviders.of(this, new CarroVmFactory(CarrosApplication.getInstance())).get(CarrosViewModel.class);
        subscriberViewModalObservable();

        if (savedInstanceState == null) {
            carrosViewModel.loadCarros(tipo);
        }
    }

    // Atualiza ao fazer o gesto Pull to Refresh
    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return () -> {
            // Valida se existe conexão ao fazer o gesto Pull to Refresh
            if (AndroidUtils.isNetworkAvailable(getContext())) {
                carrosViewModel.updateCarros(tipo);
            } else {
                swipeLayout.setRefreshing(false);
                snack(recyclerView, R.string.msg_error_conexao_indisponivel);
            }
        };
    }

    // Aqui a view se inscreve nos observáveis do viewModel para receber notificações de mudança de estado
    private void subscriberViewModalObservable() {
        // Observa o carregamento das informações da base de dados
        carrosViewModel.getLoadState().observe(getViewLifecycleOwner(), state -> {
            Log.d(TAG, "LiveEvent: getLoadState() -> " + state.status);
            handleLoadState(state);
        });

        // Observa o carregamento das informações do web service
        carrosViewModel.getUpdateState().observe(getViewLifecycleOwner(), state -> {
            Log.d(TAG, "LiveEvent: getUpdateState() -> " + state.status);
            handleUpdateState(state);
        });

        // Observa quando um ou mais carros forem deletados
        carrosViewModel.getDeleteState().observe(getViewLifecycleOwner(), state -> {
            Log.d(TAG, "LiveEvent: getDeleteState() -> " + state.status);
            handleDeleteState(state);
        });

        // Observa o compartilhamento dos carros selecionados
        carrosViewModel.getShareState().observe(getViewLifecycleOwner(), state -> {
            Log.d(TAG, "LiveEvent: getShareState()");
            handleShareState(state);
        });

        // Observa e recebe o carro selecionado pelo evento Click
        carrosViewModel.getCheckedCarro().observe(getViewLifecycleOwner(), carro -> {
            Log.d(TAG, "LiveEvent: getCheckedCarro()");
            handleCheckedCarro(carro);
        });

        // Observa e recebe o carro selecionado pelo evento LongClick
        carrosViewModel.getCheckedLongCarro().observe(getViewLifecycleOwner(), carro -> {
            Log.d(TAG, "LiveEvent: getCheckedLongCarro()");
            handleCheckedLongCarro(carro);
        });
    }

    // Trata o resultado do carregamento das informações da base de dados
    private void handleLoadState(final ViewState<List<CarroBinding>> state) {
        switch (state.status) {
            case LOADING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                carroAdapter.setCarros(state.data);
                progressBar.setVisibility(View.INVISIBLE);
                break;
            case ERROR:
                snack(recyclerView, R.string.error_operation);
                break;
        }
    }

    // Trata o resultado do carregamento das informações do web service
    private void handleUpdateState(final ViewState state) {
        switch (state.status) {
            case LOADING:
                swipeLayout.setRefreshing(true);
                break;
            case SUCCESS:
                swipeLayout.setRefreshing(false);
                break;
            case ERROR:
                swipeLayout.setRefreshing(false);
                snack(recyclerView, R.string.error_operation);
                break;
        }
    }

    // Trata o resultado de quando um ou mais carros forem deletados
    private void handleDeleteState(final ViewState<Integer> state) {
        switch (state.status) {
            case SUCCESS:
                snack(recyclerView, R.string.carros_excluidos_sucesso);
                break;
            case ERROR:
                snack(recyclerView, R.string.error_operation);
                break;
        }

        if (actionMode != null) {
            // Encerra o action mode
            actionMode.finish();
        }
    }

    // Trata o resultado do compartilhamento dos carros selecionados
    private void handleShareState(final ViewState<List<String>> state) {
        switch (state.status) {
            case LOADING:
                alertProgress = AlertUtils.progress(getContext(), getString(R.string.aguarde), getString(R.string.preparando_carros));
                alertProgress.show();
                break;
            case SUCCESS:
                if (alertProgress != null) {
                    alertProgress.dismiss();
                }

                shareSelectedCarros(state.data);
                break;
            case ERROR:
                if (alertProgress != null) {
                    alertProgress.dismiss();
                }

                snack(recyclerView, R.string.error_operation);
                break;
        }
    }

    // Trata o resultado de um carro selecionado pelo evento Click
    private void handleCheckedCarro(final CarroBinding carro) {
        if (actionMode == null) {
            openDetailsCarro(carro);
        } else { // Se a CAB está ativada
            // Atualiza o título com a quantidade de carros selecionados
            updateActionModeTitle();

            // Redesenha a lista
            if (carroAdapter != null) {
                carroAdapter.notifyDataSetChanged();
            }
        }
    }

    // Trata o resultado de um carro selecionado pelo evento LongClick
    private void handleCheckedLongCarro(CarroBinding carro) {
        if (carro != null) {
            setupActionMode();
        }
    }

    // Abre tela de detalhes do carro selecionado
    private void openDetailsCarro(CarroBinding carro) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("carro", carro);

        Intent intent = new Intent(getContext(), CarroActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    // Configura a Action Bar de Contexto (CAB)
    private void setupActionMode() {
        if (actionMode != null || carrosViewModel.getCountCheckedCarro() == 0) { return; }

        // Liga a action bar de contexto (CAB)
        actionMode = getAppCompatActivity().startSupportActionMode(getActionModeCallback());

        // Solicita ao Android para desenhar a lista novamente
        if (carroAdapter != null) {
            carroAdapter.notifyDataSetChanged();
        }

        // Atualiza o título para mostrar a quantidade de carros selecionados.
        updateActionModeTitle();
    }

    // Atualiza o título da action bar (CAB)
    private void updateActionModeTitle() {
        int countChecked = carrosViewModel.getCountCheckedCarro();

        if (countChecked == 0) {
            // Encerra o action mode
            actionMode.finish();
            return;
        }

        if (actionMode != null) {
            actionMode.setTitle(R.string.selecione_carros);

            if (countChecked == 1) {
                actionMode.setSubtitle(R.string.carro_selecionado);
            } else if (countChecked > 1) {
                actionMode.setSubtitle(getString(R.string.carros_selecionados, countChecked));
            }
        }
    }

    // Compartilha os carros selecionados
    private void shareSelectedCarros(List<String> paths) {
        ArrayList<Uri> uris = new ArrayList<>();

        for (String path : paths) {
            uris.add(Uri.parse(path));
        }

        // Cria a intent com as fotos dos carros
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        shareIntent.setType("image/*");

        // Cria o Intent Chooser com as opções
        startActivity(Intent.createChooser(shareIntent, getString(R.string.enviar_carros)));

        if (actionMode != null) {
            // Encerra o action mode
            actionMode.finish();
        }
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
                    carrosViewModel.deleteCheckedCarros();
                } else if (item.getItemId() == R.id.action_share) {
                    // Valida se existe conexão com a internet
                    if (AndroidUtils.isNetworkAvailable(getContext())) {
                        carrosViewModel.shareSelectedCarros();
                    } else {
                        snack(recyclerView, R.string.msg_error_conexao_indisponivel);
                    }
                }

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Limpa o estado
                actionMode = null;

                // Configura todos os carros para não selecionados
                carrosViewModel.uncheckCarros();

                if (carroAdapter != null) {
                    carroAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private CarroAdapter.CarroOnClickListener onClickCarro() {
        return (carro -> carrosViewModel.onCarroItemClick(carro));
    }

    private CarroAdapter.CarroOnLongClickListener onLongClickCarro() {
        return (carro -> {
            if (actionMode != null) {
                return false;
            }

            return carrosViewModel.onCarroItemLongClick(carro);
        });
    }

}
