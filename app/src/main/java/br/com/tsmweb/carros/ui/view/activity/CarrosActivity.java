package br.com.tsmweb.carros.ui.view.activity;

import android.os.Bundle;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.ui.view.fragments.CarrosFragment;

public class CarrosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carros);

        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Título
        getSupportActionBar().setTitle(getString(getIntent().getIntExtra("tipo", 0)));

        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            CarrosFragment frag = new CarrosFragment();
            frag.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }
    }
}
