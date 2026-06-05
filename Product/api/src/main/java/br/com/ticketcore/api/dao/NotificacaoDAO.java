package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Notificacao;

import java.util.List;

public interface NotificacaoDAO {
    void salvar(Notificacao notificacao);
    List<Notificacao> listarPorUsuario(Long idUsuario);
    void marcarComoLida(Long id);
}