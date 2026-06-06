package br.com.ticketcore.api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioAtualizacaoRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nmUsuario,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String dsEmail,

        String dsSenha,

        String dsFotoPerfil
) {}
