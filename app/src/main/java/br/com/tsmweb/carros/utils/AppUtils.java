package br.com.tsmweb.carros.utils;

import br.com.tsmweb.carros.R;

public final class AppUtils {

    /*
     * Converte a constante para string, para criar a URL do web service.
     */
    public static String getTipoCarroByResource(int tipo) {
        if (tipo == R.string.classicos) {
            return "classicos";
        }

        if (tipo == R.string.esportivos) {
            return "esportivos";
        }

        return "luxo";
    }

}
