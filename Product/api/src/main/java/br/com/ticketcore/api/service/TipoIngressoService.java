package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.EventoDAO;
import br.com.ticketcore.api.dao.TipoIngressoDAO;
import br.com.ticketcore.api.exception.RecursoNaoEncontradoException;
import br.com.ticketcore.api.model.TipoIngresso;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoIngressoService {

    private final TipoIngressoDAO tipoIngressoDAO;
    private final EventoDAO eventoDAO;

    public TipoIngressoService(TipoIngressoDAO tipoIngressoDAO, EventoDAO eventoDAO) {
        this.tipoIngressoDAO = tipoIngressoDAO;
        this.eventoDAO = eventoDAO;
    }

    public TipoIngresso buscarPorId(Long id) {
        return tipoIngressoDAO.buscarPorId(id);
    }

    public List<TipoIngresso> listarPorEvento(Long idEvento) {
        return tipoIngressoDAO.listarPorEvento(idEvento);
    }

    public void salvar(TipoIngresso tipoIngresso, Long idOrganizador) {
        if (tipoIngresso.getIdEvento() == null
                || eventoDAO.buscarPorIdEOrganizador(tipoIngresso.getIdEvento(), idOrganizador) == null) {
            throw new RecursoNaoEncontradoException();
        }
        tipoIngressoDAO.salvar(tipoIngresso);
    }

    public void atualizar(Long id, TipoIngresso tipoIngresso, Long idOrganizador) {
        if (tipoIngressoDAO.buscarPorIdEOrganizador(id, idOrganizador) == null) {
            throw new RecursoNaoEncontradoException();
        }
        if (tipoIngresso.getIdEvento() == null
                || eventoDAO.buscarPorIdEOrganizador(tipoIngresso.getIdEvento(), idOrganizador) == null) {
            throw new RecursoNaoEncontradoException();
        }
        tipoIngresso.setIdTipoIngresso(id);
        tipoIngressoDAO.atualizar(tipoIngresso);
    }

    public void deletar(Long id, Long idOrganizador) {
        if (tipoIngressoDAO.buscarPorIdEOrganizador(id, idOrganizador) == null) {
            throw new RecursoNaoEncontradoException();
        }
        tipoIngressoDAO.deletar(id);
    }
}
