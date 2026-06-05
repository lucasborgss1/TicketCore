package br.com.ticketcore.api.model.dto;

import br.com.ticketcore.api.model.Evento;
import java.time.LocalDateTime;

public record EventoResponse(
        Long idEvento,
        Long idOrganizador,
        Long idCategoria,
        String nmEvento,
        LocalDateTime dtEvento,
        String nmLocal,
        Integer qtCapacidade,
        String dsImagem
) {
    public static EventoResponse from(Evento e) {
        return new EventoResponse(
                e.getIdEvento(),
                e.getIdOrganizador(),
                e.getIdCategoria(),
                e.getNmEvento(),
                e.getDtEvento(),
                e.getNmLocal(),
                e.getQtCapacidade(),
                e.getDsImagem()
        );
    }
}