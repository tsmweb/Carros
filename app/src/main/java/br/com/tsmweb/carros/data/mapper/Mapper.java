package br.com.tsmweb.carros.data.mapper;

public interface Mapper<E, D> {

    D toDomain(E entity);

    E fromDomain(D domain);

}
