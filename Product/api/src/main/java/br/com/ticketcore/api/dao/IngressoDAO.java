package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Ingresso;
import java.util.List;

public interface IngressoDAO {
    void salvar(Ingresso ingresso);
    Ingresso buscarPorId(Long id);
    Ingresso buscarPorCodigoAcesso(String codigoAcesso);
    Ingresso buscarPorIdEComprador(Long idIngresso, Long idComprador);
    List<Ingresso> listarPorTransacao(Long idTransacao);
    List<Ingresso> listarPorTransacaoEComprador(Long idTransacao, Long idComprador);
    Ingresso buscarPorCodigoEComprador(String codigoAcesso, Long idComprador);
    void atualizar(Ingresso ingresso);
}