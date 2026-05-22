package br.com.ticketcore.api.model.enums;

import lombok.Getter;

@Getter
public enum StatusTransacao {
    PENDENTE("P"),
    CONCLUIDA("C"),
    CANCELADA("X");

    private final String codigo;

    StatusTransacao(String codigo) {
        this.codigo = codigo;
    }

    public static StatusTransacao fromCodigo(String codigo) {
        for (StatusTransacao status : StatusTransacao.values()) {
            if (status.getCodigo().equals(codigo)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status de transação inválido: " + codigo);
    }
}