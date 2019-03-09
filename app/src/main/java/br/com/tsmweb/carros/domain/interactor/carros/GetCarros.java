package br.com.tsmweb.carros.domain.interactor.carros;

import java.util.List;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.FlowableUseCase;
import br.com.tsmweb.carros.domain.model.Carro;
import br.com.tsmweb.carros.domain.repository.CarroRepository;
import io.reactivex.Flowable;

public class GetCarros extends FlowableUseCase<List<Carro>, GetCarros.Params> {

    private final CarroRepository carroRepository;

    public GetCarros(CarroRepository carroRepository, PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
        this.carroRepository = carroRepository;
    }

    @Override
    public Flowable<List<Carro>> buildUseCaseFlowable(Params params) {
        if (params == null) throw new IllegalArgumentException("Params can't be null");
        return carroRepository.getCarrosByTipo(params.tipo);
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
