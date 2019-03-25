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

    public static <T> ViewState<T> viewState(@NonNull Status status, @Nullable T data, @Nullable Throwable error) {
        return new ViewState<>(status, data, error);
    }

    public static <T> ViewState<T> viewState(@NonNull Status status, @Nullable T data) {
        return new ViewState<>(status, data, null);
    }

    public static <T> ViewState<T> viewState(@NonNull Status status, @Nullable Throwable error) {
        return new ViewState<>(status, null, error);
    }

    public static <T> ViewState<T> viewState(@NonNull Status status) {
        return new ViewState<>(status, null, null);
    }

    public enum Status { ERROR, LOADING, SUCCESS, INVALID }

}
