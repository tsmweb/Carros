package br.com.tsmweb.carros.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

import br.com.tsmweb.carros.utils.Prefs;

public class ExemploBackupAgent extends BackupAgentHelper {

    private static final String TAG = ExemploBackupAgent.class.getSimpleName();

    @Override
    public void onCreate() {
        // Cria um helper. Fará backup dos dados utilizando a chave Prefs.PREF_ID.
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, Prefs.PREF_ID);
        // Adiciona o helper ao agente de backups
        addHelper("livroAndroid", helper);
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        super.onBackup(oldState, data, newState);
        Log.d(TAG, "Backup efetuado com sucesso");
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        super.onRestore(data, appVersionCode, newState);
        Log.d(TAG, "Backup restaurado com sucesso.");
    }

}
