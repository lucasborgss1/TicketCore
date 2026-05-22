package br.com.ticketcore.api.model;

import br.com.ticketcore.api.model.enums.StatusTransacao;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {
    private Long idTransacao;
    private Long idComprador;
    private LocalDateTime dtTransacao;
    private StatusTransacao stTransacao;
    private BigDecimal vlTotal;
}