package br.com.tsmweb.carros.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.tsmweb.carros.R;

public class CarrosTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carros_tab, container, false);

        return view;
    }

}
