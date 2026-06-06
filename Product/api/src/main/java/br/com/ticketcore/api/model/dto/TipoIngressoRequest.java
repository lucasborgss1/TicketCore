package br.com.ticketcore.api.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TipoIngressoRequest(
        @NotNull(message = "O evento é obrigatório")
        Long idEvento,

        @NotBlank(message = "O nome do tipo de ingresso é obrigatório")
        String nmTipo,

        @NotNull(message = "O preço é obrigatório")
        @DecimalMin(value = "0.00", message = "O preço não pode ser negativo")
        BigDecimal vlPreco,

        @NotNull(message = "A quantidade do lote é obrigatória")
        @Min(value = 1, message = "O lote deve ter no mínimo 1 ingresso")
        Integer qtLote
) {}
