package br.com.ticketcore.api.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException() {
        super("Recurso não encontrado.");
    }
}
