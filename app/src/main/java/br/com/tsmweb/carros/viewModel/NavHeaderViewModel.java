package br.com.tsmweb.carros.viewModel;

import androidx.lifecycle.ViewModel;
import androidx.databinding.ObservableField;
import android.net.Uri;

public class NavHeaderViewModel extends ViewModel {

    public ObservableField<String> nome = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<Uri> foto = new ObservableField<>();

}
