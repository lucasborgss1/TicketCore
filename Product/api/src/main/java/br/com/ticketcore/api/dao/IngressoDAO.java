package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Ingresso;
import java.util.List;

public interface IngressoDAO {
    void salvar(Ingresso ingresso);
    Ingresso buscarPorId(Long id);
    Ingresso buscarPorCodigoAcesso(String codigoAcesso);
    List<Ingresso> listarPorTransacao(Long idTransacao);
    void atualizar(Ingresso ingresso);
}