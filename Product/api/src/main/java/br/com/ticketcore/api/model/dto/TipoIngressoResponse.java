package br.com.ticketcore.api.model.dto;

import br.com.ticketcore.api.model.TipoIngresso;
import java.math.BigDecimal;

public record TipoIngressoResponse(
        Long idTipoIngresso,
        Long idEvento,
        String nmTipo,
        BigDecimal vlPreco,
        Integer qtLote
) {
    public static TipoIngressoResponse from(TipoIngresso t) {
        return new TipoIngressoResponse(
                t.getIdTipoIngresso(),
                t.getIdEvento(),
                t.getNmTipo(),
                t.getVlPreco(),
                t.getQtLote()
        );
    }
}