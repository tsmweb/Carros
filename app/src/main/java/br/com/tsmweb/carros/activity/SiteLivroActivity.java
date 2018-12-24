package br.com.tsmweb.carros.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.fragments.SiteLivroFragment;
import br.com.tsmweb.carros.utils.PermissionUtils;

public class SiteLivroActivity extends BaseActivity {

    private String[] permissoesNecessarias = new String[] {
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_livro);

        // Validar permissões
        PermissionUtils.validaPermissoes(this, permissoesNecessarias, 1);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

}
