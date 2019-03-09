package br.com.tsmweb.carros.domain.interactor;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class SingleUseCase<T, Params> {

    private final PostExecutionThread postExecutionThread;
    private final CompositeDisposable compositeDisposable;

    public SingleUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
        this.compositeDisposable = new CompositeDisposable();
    }

    public abstract Single<T> buildUseCaseSingle(Params params);

    public void execute(DisposableSingleObserver<T> observer, Params params) {
        final Single<T> single = buildUseCaseSingle(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler());
        addDisposable(single.subscribeWith(observer));
    }

    public void dispose() {
        compositeDisposable.clear();
    }

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

}
