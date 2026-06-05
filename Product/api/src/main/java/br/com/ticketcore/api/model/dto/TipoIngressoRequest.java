package br.com.ticketcore.api.model.dto;

import java.math.BigDecimal;

public record TipoIngressoRequest(
        Long idEvento,
        String nmTipo,
        BigDecimal vlPreco,
        Integer qtLote
) {}