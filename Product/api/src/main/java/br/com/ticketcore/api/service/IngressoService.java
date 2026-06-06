package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.IngressoDAO;
import br.com.ticketcore.api.exception.RecursoNaoEncontradoException;
import br.com.ticketcore.api.model.Ingresso;
import br.com.ticketcore.api.model.enums.StatusIngresso;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngressoService {

    private final IngressoDAO ingressoDAO;

    public IngressoService(IngressoDAO ingressoDAO) {
        this.ingressoDAO = ingressoDAO;
    }

    public Ingresso buscarPorId(Long id, Long idComprador) {
        Ingresso ingresso = ingressoDAO.buscarPorIdEComprador(id, idComprador);
        if (ingresso == null) throw new RecursoNaoEncontradoException();
        return ingresso;
    }

    public Ingresso buscarPorCodigoAcesso(String codigoAcesso, Long idComprador) {
        Ingresso ingresso = ingressoDAO.buscarPorCodigoEComprador(codigoAcesso, idComprador);
        if (ingresso == null) throw new RecursoNaoEncontradoException();
        return ingresso;
    }

    public List<Ingresso> listarPorTransacao(Long idTransacao, Long idComprador) {
        return ingressoDAO.listarPorTransacaoEComprador(idTransacao, idComprador);
    }

    public void cancelar(Long id, Long idComprador) {
        Ingresso ingresso = ingressoDAO.buscarPorIdEComprador(id, idComprador);
        if (ingresso == null) throw new RecursoNaoEncontradoException();
        ingresso.setStIngresso(StatusIngresso.CANCELADO);
        ingressoDAO.atualizar(ingresso);
    }
}