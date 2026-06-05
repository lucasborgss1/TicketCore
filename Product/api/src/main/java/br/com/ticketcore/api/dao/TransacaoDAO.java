package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Transacao;
import java.util.List;

public interface TransacaoDAO {
    Long salvar(Transacao transacao);
    Transacao buscarPorId(Long id);
    List<Transacao> listarPorComprador(Long idComprador);
    void atualizar(Transacao transacao);
    void atualizarStatus(Long idTransacao, String status);
    void cancelarTransacao(Long idTransacao, Long idUsuarioSolicitante); // Chama a Stored Procedure
}