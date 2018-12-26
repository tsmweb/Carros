package br.com.tsmweb.carros.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.AdapterCarroBinding;
import br.com.tsmweb.carros.domain.Carro;
import br.com.tsmweb.carros.viewModel.CarrosViewModal;

public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarrosViewHolder> {

    protected final String TAG = this.getClass().getSimpleName();

    private LayoutInflater inflater;
    private List<Carro> carros;
    private CarrosViewModal viewModal;

    public CarroAdapter(CarrosViewModal viewModal) {
        this.viewModal = viewModal;
        this.carros = new ArrayList<>();
    }

    @NonNull
    @Override
    public CarrosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }

        AdapterCarroBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.adapter_carro, viewGroup, false);
        CarrosViewHolder holder = new CarrosViewHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CarrosViewHolder holder, final int position) {
        // Atualiza a view
        holder.bind(viewModal, carros.get(position));
    }

    @Override
    public int getItemCount() {
        return this.carros != null ? this.carros.size() : 0;
    }

    public List<Carro> getCarros() {
        return carros;
    }

    public void setCarros(List<Carro> carros) {
        this.carros = carros;
        notifyDataSetChanged();
    }

    // ViewHolder com as views
    public static class CarrosViewHolder extends RecyclerView.ViewHolder {

        private final AdapterCarroBinding binding;

        public CarrosViewHolder(@NonNull AdapterCarroBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(CarrosViewModal viewModal, Carro carro) {
            binding.setViewModel(viewModal);
            binding.setCarro(carro);
            binding.executePendingBindings();
        }
    }

}
