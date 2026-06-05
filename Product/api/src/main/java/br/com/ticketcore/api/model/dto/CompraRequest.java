package br.com.ticketcore.api.model.dto;

import java.util.List;

public record CompraRequest(Long idComprador, List<ItemCompra> itens) {}