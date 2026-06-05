package br.com.ticketcore.api.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ResumoVendasResponse(
        Long idEvento,
        String nmEvento,
        LocalDateTime dtEvento,
        Integer qtCapacidadeTotal,
        Integer qtIngressosVendidos,
        Integer qtCapacidadeRestante,
        BigDecimal vlReceitaTotal
) {}