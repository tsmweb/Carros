package br.com.tsmweb.carros.domain.interactor;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import io.reactivex.Completable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class CompletableUseCase<Params> extends UseCase {

    private final PostExecutionThread postExecutionThread;

    public CompletableUseCase(PostExecutionThread postExecutionThread) {
        super();
        this.postExecutionThread = postExecutionThread;
    }

    public abstract Completable buildUseCaseCompletable(Params params);

    public void execute(DisposableCompletableObserver observer, Params params) {
        disposableLast();

        final Completable completable = buildUseCaseCompletable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler());

        disposableLast = completable.subscribeWith(observer);
        addDisposable(disposableLast);
    }

}
