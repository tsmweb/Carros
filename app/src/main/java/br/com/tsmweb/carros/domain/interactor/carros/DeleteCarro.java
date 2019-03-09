package br.com.tsmweb.carros.domain.interactor.carros;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.SingleUseCase;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Single;

public class DeleteCarro extends SingleUseCase<Integer, DeleteCarro.Params> {

    private final CarroRepository carroRepository;

    public DeleteCarro(CarroRepository carroRepository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.carroRepository = carroRepository;
    }

    @Override
    public Single<Integer> buildUseCaseSingle(Params params) {
        if (params == null) throw new IllegalArgumentException("Params can't be null");
        return carroRepository.delete(params.carro);
    }

    public static final class Params {

        private Carro carro;

        private Params(Carro carro) {
            this.carro = carro;
        }

        public static Params getParams(Carro carro) {
            return new Params(carro);
        }

    }

}
