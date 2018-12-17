package br.com.tsmweb.carros.adapter;

import android.databinding.BindingAdapter;
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

}
