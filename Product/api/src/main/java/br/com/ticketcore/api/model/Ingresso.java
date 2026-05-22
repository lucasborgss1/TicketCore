package br.com.ticketcore.api.model;

import br.com.ticketcore.api.model.enums.StatusIngresso;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingresso {
    private Long idIngresso;
    private Long idTransacao;
    private Long idTipoIngresso;
    private String cdAcesso;
    private StatusIngresso stIngresso;
}