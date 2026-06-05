package br.com.ticketcore.api.model.dto;

import br.com.ticketcore.api.model.Categoria;

public record CategoriaResponse(Long idCategoria, String nmCategoria, String dsCategoria) {
    public static CategoriaResponse from(Categoria c) {
        return new CategoriaResponse(
                c.getIdCategoria(),
                c.getNmCategoria(),
                c.getDsCategoria());
    }
}