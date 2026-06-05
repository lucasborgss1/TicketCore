package br.com.ticketcore.api.model.dto;

public record LoginResponse(String token, Long idUsuario, String nmUsuario) {}
