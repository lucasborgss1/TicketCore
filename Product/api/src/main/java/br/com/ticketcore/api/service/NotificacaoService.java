package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.NotificacaoDAO;
import br.com.ticketcore.api.model.Notificacao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoDAO notificacaoDAO;

    public NotificacaoService(NotificacaoDAO notificacaoDAO) {
        this.notificacaoDAO = notificacaoDAO;
    }

    public List<Notificacao> listarPorUsuario(Long idUsuario) {
        return notificacaoDAO.listarPorUsuario(idUsuario);
    }

    public void marcarComoLida(Long id) {
        notificacaoDAO.marcarComoLida(id);
    }
}
