package br.com.tsmweb.carros.domain.interactor.carros;

import java.util.List;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.SingleUseCase;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Single;

public class DeleteCarros extends SingleUseCase<Integer, DeleteCarros.Params> {

    private final CarroRepository carroRepository;

    public DeleteCarros(CarroRepository carroRepository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.carroRepository = carroRepository;
    }

    @Override
    public Single<Integer> buildUseCaseSingle(Params params) {
        if (params == null) throw new IllegalArgumentException("Params can't be null");
        return carroRepository.delete(params.carros);
    }

    public static final class Params {

        private List<Carro> carros;

        private Params(List<Carro> carros) {
            this.carros = carros;
        }

        public static Params getParams(List<Carro> carros) {
            return new Params(carros);
        }

    }

}
