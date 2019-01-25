package br.com.tsmweb.carros.domain.repository;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IRepository<T, K> {

    Single<K> save(T item);

    Single<Integer> delete(T item);

    Maybe<T> getById(K id);

    Flowable<List<T>> getAll();

}
