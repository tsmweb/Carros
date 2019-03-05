package br.com.tsmweb.carros.presentation.viewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewState<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;


    public ViewState(@NonNull Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> ViewState<T> success(@NonNull T data) {
        return new ViewState<>(Status.SUCCESS, data, null);
    }

    public static <T> ViewState<T> error(@Nullable T data) {
        return new ViewState<>(Status.ERROR, data, null);
    }

    public static <T> ViewState<T> error(@Nullable Throwable error, @Nullable T data) {
        return new ViewState<>(Status.ERROR, data, error);
    }

    public static <T> ViewState<T> loading(@Nullable T data) {
        return new ViewState<>(Status.LOADING, data, null);
    }

    public static <T> ViewState<T> update(@Nullable T data) {
        return new ViewState<>(Status.UPDATE, data, null);
    }

    public static <T> ViewState<T> delete(@Nullable T data) {
        return new ViewState<>(Status.DELETE, data, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING, UPDATE, DELETE }

}
