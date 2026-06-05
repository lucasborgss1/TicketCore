package br.com.ticketcore.api.model.dto;

public record UsuarioCadastroRequest(
        String nmUsuario,
        String dsEmail,
        String dsSenha,
        String nuCpfCnpj,
        String dsFotoPerfil
) {}