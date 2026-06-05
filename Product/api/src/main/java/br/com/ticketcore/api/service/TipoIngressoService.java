package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.TipoIngressoDAO;
import br.com.ticketcore.api.model.TipoIngresso;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoIngressoService {

    private final TipoIngressoDAO tipoIngressoDAO;

    public TipoIngressoService(TipoIngressoDAO tipoIngressoDAO) {
        this.tipoIngressoDAO = tipoIngressoDAO;
    }

    public TipoIngresso buscarPorId(Long id) {
        return tipoIngressoDAO.buscarPorId(id);
    }

    public List<TipoIngresso> listarPorEvento(Long idEvento) {
        return tipoIngressoDAO.listarPorEvento(idEvento);
    }

    public void salvar(TipoIngresso tipoIngresso) {
        tipoIngressoDAO.salvar(tipoIngresso);
    }

    public void atualizar(Long id, TipoIngresso tipoIngresso) {
        tipoIngresso.setIdTipoIngresso(id);
        tipoIngressoDAO.atualizar(tipoIngresso);
    }

    public void deletar(Long id) {
        tipoIngressoDAO.deletar(id);
    }
}
