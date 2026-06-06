package br.com.ticketcore.api.model.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String nmCategoria,

        String dsCategoria
) {}
