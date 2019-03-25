package br.com.tsmweb.carros.presentation.mapper;

public interface Mapper<D, B> {

    D toDomain(B binding);

    B fromDomain(D domain);
}
