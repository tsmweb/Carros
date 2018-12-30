package br.com.tsmweb.carros.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.utils.AlertUtils;
import br.com.tsmweb.carros.utils.PermissionUtils;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Lista de permissões necessárias
        String permissions[] = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        // Valida lista de permissões
        boolean ok = PermissionUtils.validaPermissoes(this, permissions,0);

        if (ok) {
            // Tudo OK, pode entrar
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Negou a permissão. Mostra alerta e fecha.
                AlertUtils.alert(getContext(), R.string.app_name, R.string.msg_alerta_permissao, R.string.ok, () -> {
                    // Negou permissão. Sai do app.
                    finish();
                });

                return;
            }
        }

        // Permissões concedidas. Pode entrar.
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
