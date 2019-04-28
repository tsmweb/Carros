package br.com.tsmweb.carros.view.activity;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import br.com.tsmweb.carros.CarrosApplication;
import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.ActivityCarroBinding;
import br.com.tsmweb.carros.presentation.model.CarroBinding;
import br.com.tsmweb.carros.presentation.viewModel.CarroVmFactory;
import br.com.tsmweb.carros.view.fragments.CarroFragment;
import br.com.tsmweb.carros.presentation.viewModel.CarroViewModel;

public class CarroActivity extends BaseActivity {

    private ActivityCarroBinding binding;
    private CarroViewModel carroViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_carro);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        CarroBinding carro = bundle.getParcelable("carro");

        // Configura a Toolbar como a action bar
        setUpToolbar();
        // Botão up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configura o ViewModal
        carroViewModel = ViewModelProviders.of(this, new CarroVmFactory(CarrosApplication.getInstance())).get(CarroViewModel.class);

        subscriberViewModalObservable();

        // Adiciona o fragment ao layout
        if (savedInstanceState == null) {
            carroViewModel.setCarro(carro);

            // Cria o fragment com o mesmo Bundle (args) da intent
            CarroFragment frag = new CarroFragment();
            frag.setArguments(getIntent().getExtras());
            // Adiciona o fragment ao layout
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.carro_fragment, frag)
                    .commit();
        }
    }

    private void subscriberViewModalObservable() {
        carroViewModel.getLoadState().observe(this, state -> {
            switch (state.status) {
                case SUCCESS:
                    binding.setCarro(state.data);
                    break;
            }
        });
    }

}
