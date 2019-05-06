package br.com.tsmweb.carros.domain.interactor;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class SingleUseCase<T, Params> extends UseCase {

    private final PostExecutionThread postExecutionThread;

    public SingleUseCase(PostExecutionThread postExecutionThread) {
        super();
        this.postExecutionThread = postExecutionThread;
    }

    public abstract Single<T> buildUseCaseSingle(Params params);

    public void execute(DisposableSingleObserver<T> observer, Params params) {
        disposableLast();

        final Single<T> single = buildUseCaseSingle(params)
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler());

        disposableLast = single.subscribeWith(observer);
        addDisposable(disposableLast);
    }

}
