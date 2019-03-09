package br.com.tsmweb.carros.domain.interactor.carros;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.CompletableUseCase;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Completable;

public class UpdateCarros extends CompletableUseCase<UpdateCarros.Params> {

    private final CarroRepository carroRepository;

    public UpdateCarros(CarroRepository carroRepository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.carroRepository = carroRepository;
    }

    @Override
    public Completable buildUseCaseCompletable(Params params) {
        if (params == null) throw new IllegalArgumentException("Params can't be null");
        return carroRepository.updateCarros(params.tipo);
    }

    public static final class Params {

        private int tipo;

        private Params(int tipo) {
            this.tipo = tipo;
        }

        public static Params getParams(int tipo) {
            return new Params(tipo);
        }

    }

}
