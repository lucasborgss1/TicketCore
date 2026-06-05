package br.com.ticketcore.api.model.dto;

import br.com.ticketcore.api.model.Transacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponse(
        Long idTransacao,
        Long idComprador,
        LocalDateTime dtTransacao,
        String status,
        BigDecimal vlTotal
) {
    public static TransacaoResponse from(Transacao t) {
        return new TransacaoResponse(
                t.getIdTransacao(), t.getIdComprador(), t.getDtTransacao(),
                t.getStTransacao().name(), t.getVlTotal()
        );
    }
}