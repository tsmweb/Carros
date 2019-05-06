package br.com.tsmweb.carros.ui.view.adapter;

import androidx.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


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
