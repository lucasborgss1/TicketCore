package br.com.ticketcore.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfil {
    private Long idUsuario;
    private Long idPerfil;
}