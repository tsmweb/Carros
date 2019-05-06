package br.com.tsmweb.carros.ui.presentation.mapper;

public interface Mapper<D, B> {

    D toDomain(B binding);

    B fromDomain(D domain);
}
