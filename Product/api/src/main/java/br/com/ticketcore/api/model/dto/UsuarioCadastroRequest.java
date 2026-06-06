package br.com.ticketcore.api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nmUsuario,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String dsEmail,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String dsSenha,

        @NotBlank(message = "O CPF/CNPJ é obrigatório")
        @Size(min = 11, max = 14, message = "CPF deve ter 11 dígitos; CNPJ deve ter 14")
        String nuCpfCnpj,

        String dsFotoPerfil
) {}
