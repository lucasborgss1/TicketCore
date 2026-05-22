package br.com.ticketcore.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    private Long idEvento;
    private Long idOrganizador;
    private Long idCategoria;
    private String nmEvento;
    private LocalDateTime dtEvento;
    private String nmLocal;
    private Integer qtCapacidade;
    private String dsImagem;
}