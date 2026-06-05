package br.com.ticketcore.api.model.dto;

public record CompraIniciadaResponse(
        Long idTransacao,
        String mensagem
) {}