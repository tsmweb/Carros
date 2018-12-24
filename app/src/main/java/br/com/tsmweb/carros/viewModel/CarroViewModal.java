package br.com.tsmweb.carros.viewModel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import br.com.tsmweb.carros.domain.Carro;

public class CarroViewModal extends ViewModel {

    public ObservableField<String> descricao = new ObservableField<>();
    public ObservableField<String> urlFoto = new ObservableField<>();

    public void setCarro(Carro carro) {
        descricao.set(carro.getDesc());
        urlFoto.set(carro.getUrlFoto());
    }

}
