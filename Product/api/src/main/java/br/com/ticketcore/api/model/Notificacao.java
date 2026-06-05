package br.com.ticketcore.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {
    private Long idNotificacao;
    private Long idUsuario;
    private String dsMensagem;
    private String chLida;
    private LocalDateTime dtCriacao;
}