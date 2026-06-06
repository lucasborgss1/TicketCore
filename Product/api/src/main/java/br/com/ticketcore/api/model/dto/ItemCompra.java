package br.com.ticketcore.api.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemCompra(
        @NotNull(message = "O tipo de ingresso é obrigatório")
        Long idTipoIngresso,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade mínima é 1")
        Integer quantidade
) {}
