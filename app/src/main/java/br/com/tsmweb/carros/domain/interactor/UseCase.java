package br.com.tsmweb.carros.domain.interactor;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class UseCase {

    protected final CompositeDisposable compositeDisposable;
    protected Disposable disposableLast;

    public UseCase() {
        this.compositeDisposable = new CompositeDisposable();
    }

    protected void disposableLast() {
        if (disposableLast != null && !disposableLast.isDisposed()) {
            disposableLast.dispose();
        }
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        compositeDisposable.clear();
    }
}
