package br.com.ticketcore.api.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CompraRequest(
        @NotEmpty(message = "A lista de itens não pode ser vazia")
        List<@Valid ItemCompra> itens
) {}
