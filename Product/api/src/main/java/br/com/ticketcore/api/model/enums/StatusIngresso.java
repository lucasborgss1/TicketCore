package br.com.ticketcore.api.model.enums;

import lombok.Getter;

@Getter
public enum StatusIngresso {
    ATIVO("A"),
    UTILIZADO("U"),
    CANCELADO("C");

    private final String codigo;

    StatusIngresso(String codigo) {
        this.codigo = codigo;
    }

    public static StatusIngresso fromCodigo(String codigo) {
        for (StatusIngresso status : StatusIngresso.values()) {
            if (status.getCodigo().equals(codigo)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de ingresso inválido: " + codigo);
    }
}