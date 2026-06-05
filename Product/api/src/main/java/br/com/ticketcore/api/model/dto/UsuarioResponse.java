package br.com.ticketcore.api.model.dto;

import br.com.ticketcore.api.model.Usuario;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long idUsuario,
        String nmUsuario,
        String dsEmail,
        String nuCpfCnpj,
        String dsFotoPerfil,
        LocalDateTime dtCadastro
) {
    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(
                u.getIdUsuario(),
                u.getNmUsuario(),
                u.getDsEmail(),
                u.getNuCpfCnpj(),
                u.getDsFotoPerfil(),
                u.getDtCadastro()
        );
    }
}