package br.com.ticketcore.api.model.dto;

import br.com.ticketcore.api.model.Ingresso;

public record IngressoResponse(
        Long idIngresso,
        Long idTransacao,
        Long idTipoIngresso,
        String cdAcesso,
        String status
) {
    public static IngressoResponse from(Ingresso i) {
        return new IngressoResponse(
                i.getIdIngresso(), i.getIdTransacao(), i.getIdTipoIngresso(),
                i.getCdAcesso(), i.getStIngresso().name()
        );
    }
}