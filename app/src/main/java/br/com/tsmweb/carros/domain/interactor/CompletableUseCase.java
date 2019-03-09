package br.com.tsmweb.carros.domain.interactor;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class CompletableUseCase<Params> {

    private final PostExecutionThread postExecutionThread;
    private final CompositeDisposable compositeDisposable;

    public CompletableUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
        this.compositeDisposable = new CompositeDisposable();
    }

    public abstract Completable buildUseCaseCompletable(Params params);

    public void execute(DisposableCompletableObserver observer, Params params) {
        final Completable completable = buildUseCaseCompletable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler());
        addDisposable(completable.subscribeWith(observer));
    }

    public void dispose() {
        compositeDisposable.clear();
    }

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

}
