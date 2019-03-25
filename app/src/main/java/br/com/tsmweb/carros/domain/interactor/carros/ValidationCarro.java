package br.com.tsmweb.carros.domain.interactor.carros;

import br.com.tsmweb.carros.domain.executor.PostExecutionThread;
import br.com.tsmweb.carros.domain.interactor.SingleUseCase;
import br.com.tsmweb.carros.domain.model.Carro;
import io.reactivex.Single;

public class ValidationCarro extends SingleUseCase<String, ValidationCarro.Params> {

    public ValidationCarro(PostExecutionThread postExecutionThread) {
        super(postExecutionThread);
    }

    @Override
    public Single<String> buildUseCaseSingle(Params params) {
        if (params == null) throw new IllegalArgumentException("Params can't be null");

        return Single.fromCallable(() -> {
            if (params.carro.getNome().trim().length() == 0) {
                return "nome";
            }

            return null;
        });
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
