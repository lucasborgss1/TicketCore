package br.com.ticketcore.api.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EventoRequest(
        @NotNull(message = "A categoria é obrigatória")
        Long idCategoria,

        @NotBlank(message = "O nome do evento é obrigatório")
        String nmEvento,

        @NotNull(message = "A data do evento é obrigatória")
        @Future(message = "A data do evento deve ser no futuro")
        LocalDateTime dtEvento,

        @NotBlank(message = "O local do evento é obrigatório")
        String nmLocal,

        @NotNull(message = "A capacidade é obrigatória")
        @Min(value = 1, message = "A capacidade mínima é 1")
        Integer qtCapacidade,

        String dsImagem
) {}
