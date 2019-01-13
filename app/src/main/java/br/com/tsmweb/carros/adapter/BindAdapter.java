package br.com.tsmweb.carros.adapter;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.tsmweb.carros.R;


public class BindAdapter {

    @BindingAdapter("android:src")
    public static void bindUriToImageView(ImageView imageView, Uri uri) {
        Picasso.get()
                .load(uri)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    @BindingAdapter("android:src")
    public static void bindUrlToImageView(ImageView imageView, String url) {
        Picasso.get()
                .load(url)
                .into(imageView);
    }

}
