package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Webhook;
import java.util.List;

public interface WebhookDAO {
    void salvar(Webhook webhook);
    Webhook buscarPorId(Long id);
    List<Webhook> listarPorUsuario(Long idUsuario);
    List<Webhook> listarAtivosPorTipoEvento(String tipoEvento);
    void atualizar(Webhook webhook);
    void deletar(Long id);
}