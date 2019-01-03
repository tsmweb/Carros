package br.com.tsmweb.carros.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.ActivityCarroBinding;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.fragments.CarroFragment;
import br.com.tsmweb.carros.viewModel.CarroViewModal;

public class CarroActivity extends BaseActivity {

    private CarroViewModal carroViewModal;

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
        carroViewModal = ViewModelProviders.of(this).get(CarroViewModal.class);
        binding.setViewModal(carroViewModal);
        carroViewModal.setCarro(carro);

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
