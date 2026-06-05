package br.com.ticketcore.api.model.dto;

import java.time.LocalDateTime;

public record EventoRequest(
        Long idOrganizador,
        Long idCategoria,
        String nmEvento,
        LocalDateTime dtEvento,
        String nmLocal,
        Integer qtCapacidade,
        String dsImagem
) {}