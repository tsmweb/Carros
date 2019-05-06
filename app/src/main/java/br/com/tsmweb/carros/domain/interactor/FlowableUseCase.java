package br.com.tsmweb.carros.domain.interactor;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public abstract class FlowableUseCase<T, Params> extends UseCase {

    private final PostExecutionThread postExecutionThread;

    public FlowableUseCase(PostExecutionThread postExecutionThread) {
        super();
        this.postExecutionThread = postExecutionThread;
    }

    public abstract Flowable<T> buildUseCaseFlowable(Params params);

    public void execute(DisposableSubscriber<T> observer, Params params) {
        disposableLast();

        final Flowable<T> flowable = buildUseCaseFlowable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler());

        disposableLast = flowable.subscribeWith(observer);
        addDisposable(disposableLast);
    }

}
