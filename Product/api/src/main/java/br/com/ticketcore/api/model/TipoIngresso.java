package br.com.ticketcore.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoIngresso {
    private Long idTipoIngresso;
    private Long idEvento;
    private String nmTipo;
    private BigDecimal vlPreco;
    private Integer qtLote;
}