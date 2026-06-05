package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.IngressoDAO;
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

    public Ingresso buscarPorId(Long id) {
        return ingressoDAO.buscarPorId(id);
    }

    public Ingresso buscarPorCodigoAcesso(String codigoAcesso) {
        return ingressoDAO.buscarPorCodigoAcesso(codigoAcesso);
    }

    public List<Ingresso> listarPorTransacao(Long idTransacao) {
        return ingressoDAO.listarPorTransacao(idTransacao);
    }

    public void cancelar(Long id) {
        Ingresso ingresso = ingressoDAO.buscarPorId(id);
        ingresso.setStIngresso(StatusIngresso.CANCELADO);
        ingressoDAO.atualizar(ingresso);
    }
}