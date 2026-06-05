package br.com.ticketcore.api.model.dto;

import br.com.ticketcore.api.model.Notificacao;
import java.time.LocalDateTime;

public record NotificacaoResponse(
        Long idNotificacao,
        String dsMensagem,
        boolean lida,
        LocalDateTime dtCriacao
) {
    public static NotificacaoResponse from(Notificacao n) {
        return new NotificacaoResponse(
                n.getIdNotificacao(), n.getDsMensagem(),
                "S".equals(n.getChLida()), n.getDtCriacao()
        );
    }
}