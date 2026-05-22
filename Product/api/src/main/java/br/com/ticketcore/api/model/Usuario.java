package br.com.ticketcore.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long idUsuario;
    private String nmUsuario;
    private String dsEmail;
    private String dsSenha;
    private String nuCpfCnpj;
    private String dsFotoPerfil;
    private LocalDateTime dtCadastro;
}