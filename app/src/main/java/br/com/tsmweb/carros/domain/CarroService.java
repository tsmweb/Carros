package br.com.tsmweb.carros.domain;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CarroService {

    public static List<Carro> getCarros(Context context, int tipo) {
        String tipoString = context.getString(tipo);
        List<Carro> carros = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Carro c = new Carro();
            c.setNome("Carro " + tipoString + ": " + i);
            c.setDesc("Desc " + i);
            c.setUrlFoto("http://www.livroandroid.com.br/livro/carros/esportivos/Ferrari_FF.png");

            carros.add(c);
        }

        return carros;
    }
}
