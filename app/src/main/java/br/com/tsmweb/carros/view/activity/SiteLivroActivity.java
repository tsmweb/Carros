package br.com.tsmweb.carros.view.activity;

import android.os.Bundle;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.view.fragments.SiteLivroFragment;

public class SiteLivroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_livro);

        setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Título
        getSupportActionBar().setTitle(getString(R.string.site_do_livro));

        // Adiciona o fragment
        if (savedInstanceState == null) {
            SiteLivroFragment frag = new SiteLivroFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.site_fragment, frag)
                    .commit();
        }
    }

}
