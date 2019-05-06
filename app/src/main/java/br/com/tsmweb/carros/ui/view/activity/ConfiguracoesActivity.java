package br.com.tsmweb.carros.ui.view.activity;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import br.com.tsmweb.carros.R;

public class ConfiguracoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Adiciona o fragment de configurações
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, new PrefsFragment());
        ft.commit();
    }

    public static class PrefsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            // Carrega as configurações
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
