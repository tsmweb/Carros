package br.com.tsmweb.carros.domain;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.domain.dao.CarroDAO;
import br.com.tsmweb.carros.domain.dao.CarrosDatabase;
import br.com.tsmweb.carros.utils.FileUtils;
import br.com.tsmweb.carros.utils.HttpHelper;
import br.com.tsmweb.carros.utils.IOUtils;
import br.com.tsmweb.carros.utils.SDCardUtils;

@Deprecated
public class CarrosAPI {

    private static final String TAG = CarrosAPI.class.getSimpleName();
    private static final String URL = "http://www.livroandroid.com.br/livro/carros/carros_{tipo}.json";

    public static List<Carro> getCarros(Context context, int tipo, boolean refresh) throws Exception {
        //List<Carro> carros = getCarrosFromArquivo(context, tipo);
        // Busca os carros no banco de dados (somente se refresh = false)
        List<Carro> carros = !refresh ? getCarrosFromBanco(context, tipo) : null;

        if (carros != null && carros.size() > 0) {
           // Retorna os carros encontrados
           return carros;
        }

        // Se não encontrar, busca no web service
        carros = getCarrosFromWebService(context, tipo);

        return carros;
    }

    // Busca os carros do banco de dados
    public static List<Carro> getCarrosFromBanco(Context context, int tipo) throws Exception {
        String tipoString = getTipo(tipo);
       /*
        List<Carro> carros = CarrosDatabase
                                .getCarroDatabase(context)
                                .carroDAO()
                                .findAllByTipo(tipoString);

        Log.d(TAG, "Retornando " + carros.size() + " carros ["+ tipoString + "] do banco");
        */
        return Collections.emptyList();
    }

    // Abre o arquivo salvo, se existir, e cria a lista de carros.
    public static List<Carro> getCarrosFromArquivo(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String fileName = String.format("carros_%s.json", tipoString);
        Log.d(TAG, "Abrindo arquivo: " + fileName);

        // Lê o arquivo da memória interna
        String json = FileUtils.readFile(context, fileName, "UTF-8");

        if (json == null) {
            Log.d(TAG, "Arquivo " + fileName + " não encontrado.");
            return null;
        }

        List<Carro> carros = parserJSON(json);
        Log.d(TAG, "Retornando carros do arquivo " + fileName + ".");

        return carros;
    }

    // Faz a requisição HTTP, cria a lista de carros e salva o JSON em arquivo.
    public static List<Carro> getCarrosFromWebService(Context context, int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String url = URL.replace("{tipo}", tipoString);
        Log.d(TAG, "URL: " + url);

        // Faz a requisição HTTP no servidor e retorna a string com o conteúdo
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);

        List<Carro> carros = parserJSON(json);

        // Salva o texto do JSON em arquivo
        //salvaArquivoNaMemoriaInterna(context, url, json);
        //salvaArquivoNaMemoriaExterna(context, url, json);

        // Salva os carros no banco de dados
        salvarCarros(context, tipo, carros);

        return carros;
    }

    // Faz a requisição HTTP, cria a lista de carros e salva o JSON em arquivo.
    public static List<Carro> getCarrosFromWebService(int tipo) throws IOException {
        String tipoString = getTipo(tipo);
        String url = URL.replace("{tipo}", tipoString);
        Log.d(TAG, "URL: " + url);

        // Faz a requisição HTTP no servidor e retorna a string com o conteúdo
        HttpHelper http = new HttpHelper();
        String json = http.doGet(url);

        List<Carro> carros = parserJSON(json);

        return carros;
    }

    // Salva os carros no banco de dados
    private static void salvarCarros(Context context, int tipo, List<Carro> carros) {
        CarroDAO carroDAO = CarrosDatabase.getCarroDatabase(context).carroDAO();

        // Deleta os carros antigos pelo tipo para limpar o banco
        String tipoString = getTipo(tipo);
        carroDAO.deleteCarrosByTipo(tipoString);

        // Salva todos os carros
        for (Carro carro : carros) {
            carro.setTipo(tipoString);
            long id = carroDAO.save(carro);
            carro.setId(id);

            Log.d(TAG, "Salvando o carro " + carro.getId() + " - "+ carro.getNome());
        }
    }

    private static void salvaArquivoNaMemoriaInterna(Context context, String url, String json) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        File file = FileUtils.getFile(context, fileName);
        IOUtils.writeString(file, json);
        Log.d(TAG, "Arquivo salvo: " + file);
    }

    private static void salvaArquivoNaMemoriaExterna(Context context, String url, String json) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);

        // Cria um arquivo privado
        File f = SDCardUtils.getPrivateFile(context, fileName, Environment.DIRECTORY_DOWNLOADS);
        IOUtils.writeString(f, json);
        Log.d(TAG, "1) Arquivo privado salvo na pasta downloads: " + f);

        // Cria um arquivo público
        f = SDCardUtils.getPublicFile(fileName, Environment.DIRECTORY_DOWNLOADS);
        IOUtils.writeString(f, json);
        Log.d(TAG, "2) Arquivo público salvo na pasta downloads: " + f);
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

    private static List<Carro> parserJSON(String json) throws IOException {
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
