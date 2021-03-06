package br.com.tsmweb.carros.ui.view.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.tsmweb.carros.R;
import br.com.tsmweb.carros.databinding.AdapterCarroBinding;
import br.com.tsmweb.carros.ui.presentation.model.CarroBinding;

public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarrosViewHolder> {

    private static final String TAG = CarroAdapter.class.getSimpleName();

    private LayoutInflater inflater;
    private List<CarroBinding> carros;
    private Context context;

    private CarroOnClickListener onClickListener;
    private CarroOnLongClickListener onLongClickListener;

    public CarroAdapter(@NonNull Context context, CarroOnClickListener onClickListener, CarroOnLongClickListener onLongClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;

        this.carros = new ArrayList<>();
    }

    @NonNull
    @Override
    public CarrosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }

        AdapterCarroBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_carro, viewGroup, false);
        CarrosViewHolder holder = new CarrosViewHolder(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CarrosViewHolder holder, final int position) {
        CarroBinding carro = carros.get(position);

        // Atualiza a view
        holder.bind(carro);

        holder.binding.progressImg.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(carro.getUrlFoto())
                .fit()
                .into(holder.binding.imgFoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.binding.progressImg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                       // holder.binding.progressImg.setVisibility(View.GONE);
                    }
                });

        // Pinta o fundo de azul se a linha estiver selecionada
        int corFundo = context.getResources().getColor(carro.selected ? R.color.primary : R.color.white);
        holder.binding.cardView.setBackgroundColor(corFundo);

        // A cor do texto é dark gray ou branca, depende da cor do fundo.
        int corFonte = context.getResources().getColor(carro.selected ? R.color.white : R.color.dark_gray);
        holder.binding.txtNome.setTextColor(corFonte);

        // Listener para os eventos de click e long click
        if (onClickListener != null) {
            holder.binding.cardView.setOnClickListener(v -> onClickListener.onClickCarro(carro));
        }

        if (onLongClickListener != null) {
            holder.binding.cardView.setOnLongClickListener(v -> onLongClickListener.onLongClickCarro(carro));
        }
    }

    @Override
    public int getItemCount() {
        return this.carros != null ? this.carros.size() : 0;
    }

    public void setCarros(@NonNull List<CarroBinding> carros) {
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

        public void bind(CarroBinding carro) {
            binding.setCarro(carro);
            binding.executePendingBindings();
        }
    }

    public interface CarroOnClickListener {
        void onClickCarro(CarroBinding carro);
    }

    public interface CarroOnLongClickListener {
        boolean onLongClickCarro(CarroBinding carro);
    }

}
