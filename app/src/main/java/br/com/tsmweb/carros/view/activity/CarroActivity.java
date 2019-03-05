package br.com.tsmweb.carros.view.activity;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.ActivityCarroBinding;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.view.fragments.CarroFragment;
import br.com.tsmweb.carros.presentation.viewModel.CarroViewModel;

public class CarroActivity extends BaseActivity {

    private CarroViewModel carroViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCarroBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_carro);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        Carro carro = bundle.getParcelable("carro");

        // Configura a Toolbar como a action bar
        setUpToolbar();
        // Botão up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configura o ViewModal
        carroViewModel = ViewModelProviders.of(this).get(CarroViewModel.class);
        binding.setViewModal(carroViewModel);
        carroViewModel.setCarro(carro);

        // Adiciona o fragment ao layout
        if (savedInstanceState == null) {
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

}
