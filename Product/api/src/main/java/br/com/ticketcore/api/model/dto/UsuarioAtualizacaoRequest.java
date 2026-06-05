package br.com.ticketcore.api.model.dto;

public record UsuarioAtualizacaoRequest(
        String nmUsuario,
        String dsEmail,
        String dsSenha,
        String dsFotoPerfil
) {}