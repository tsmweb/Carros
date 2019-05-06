package br.com.tsmweb.carros.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

public class ImageUtils {

    public static final Uri getUriToDrawable(@NonNull Context context, @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

}
