package br.com.tsmweb.carros.domain;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.utils.HttpHelper;

public class CarroService {

    private static final String URL = "http://www.livroandroid.com.br/livro/carros/carros_{tipo}.json";

    public static List<Carro> getCarros(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String url = URL.replace("{tipo}", tipoString);

        // Faz a requisição HTTP no servidor e retorna a string com o conteúdo
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);

        List<Carro> carros = parserJSON(context, json);

        return carros;
    }

    // Converte a constante para string, para criar a URL do web service.
    private static String getTipo(int tipo) {
        if (tipo == R.string.classicos) {
            return "classicos";
        }

        if (tipo == R.string.esportivos) {
            return "esportivos";
        }

        return "luxo";
    }

    private static List<Carro> parserJSON(Context context, String json) throws IOException {
        List<Carro> carros = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(json);
            JSONObject obj = root.getJSONObject("carros");
            JSONArray jsonCarros = obj.getJSONArray("carro");

            // Insere cada carro na lista
            for (int i = 0; i < jsonCarros.length(); i++) {
                JSONObject jsonCarro = jsonCarros.getJSONObject(i);
                Carro c = new Carro();
                // Lê as informações de cada carro
                c.setNome(jsonCarro.optString("nome"));
                c.setDesc(jsonCarro.optString("desc"));
                c.setUrlFoto(jsonCarro.optString("url_foto"));
                c.setUrlInfo(jsonCarro.optString("url_info"));
                c.setUrlVideo(jsonCarro.optString("url_video"));
                c.setLatitude(jsonCarro.optString("latitude"));
                c.setLongitude(jsonCarro.optString("longitude"));

                carros.add(c);
            }

        } catch (JSONException e) {
            throw new IOException(e.getMessage(), e);
        }

        return carros;
    }

}
