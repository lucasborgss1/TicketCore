package br.com.ticketcore.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Webhook {
    private Long idWebhook;
    private Long idUsuario;
    private String dsUrl;
    private String tpEvento;
    private String chAtivo; // 'S' ou 'N'
}