package br.com.ticketcore.api.model.dto;

import br.com.ticketcore.api.model.Perfil;

public record PerfilResponse(Long idPerfil, String nmPerfil) {
    public static PerfilResponse from(Perfil p) {
        return new PerfilResponse(
                p.getIdPerfil(),
                p.getNmPerfil());
    }
}