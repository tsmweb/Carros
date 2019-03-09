package br.com.tsmweb.carros.domain.interactor;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public abstract class FlowableUseCase<T, Params> {

    private final PostExecutionThread postExecutionThread;
    private final CompositeDisposable compositeDisposable;

    public FlowableUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
        this.compositeDisposable = new CompositeDisposable();
    }

    public abstract Flowable<T> buildUseCaseFlowable(Params params);

    public void execute(DisposableSubscriber<T> observer, Params params) {
        final Flowable<T> flowable = buildUseCaseFlowable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler());
        addDisposable(flowable.subscribeWith(observer));
    }

    public void dispose() {
        compositeDisposable.clear();
    }

    private void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

}
