package br.com.tsmweb.carros.data.data_remote.service;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarrosApi {

    public static final String BASE_URL = "http://www.livroandroid.com.br/livro/carros/";

    private Retrofit retrofit;

    public CarrosApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public CarrosService getCarrosService() {
        return retrofit.create(CarrosService.class);
    }

}
