package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.EventoDAO;
import br.com.ticketcore.api.dao.PerfilDAO;
import br.com.ticketcore.api.dao.UsuarioPerfilDAO;
import br.com.ticketcore.api.exception.RecursoNaoEncontradoException;
import br.com.ticketcore.api.model.Evento;
import br.com.ticketcore.api.model.Perfil;
import br.com.ticketcore.api.model.UsuarioPerfil;
import br.com.ticketcore.api.model.dto.ResumoVendasResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EventoService {

    private static final Logger log = LoggerFactory.getLogger(EventoService.class);
    private final EventoDAO eventoDAO;
    private final PerfilDAO perfilDAO;
    private final UsuarioPerfilDAO usuarioPerfilDAO;

    public EventoService(EventoDAO eventoDAO,
                         PerfilDAO perfilDAO,
                         UsuarioPerfilDAO usuarioPerfilDAO) {
        this.eventoDAO = eventoDAO;
        this.perfilDAO = perfilDAO;
        this.usuarioPerfilDAO = usuarioPerfilDAO;
    }

    public List<Evento> listarTodos() {
        return eventoDAO.listarTodos();
    }

    public Evento buscarPorId(Long id) {
        return eventoDAO.buscarPorId(id);
    }

    public List<Evento> listarPorOrganizador(Long idOrganizador) {
        return eventoDAO.listarPorOrganizador(idOrganizador);
    }

    public void salvar(Evento evento) {
        if (evento.getDtEvento() == null || !evento.getDtEvento().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data do evento deve ser uma data futura.");
        }
        eventoDAO.salvar(evento);

        Perfil organizadorPerfil = perfilDAO.buscarPorNome("ORGANIZADOR");
        if (organizadorPerfil != null) {
            List<UsuarioPerfil> perfisAtuais =
                    usuarioPerfilDAO.buscarPerfisPorUsuario(evento.getIdOrganizador());
            boolean jaEhOrganizador = perfisAtuais.stream()
                    .anyMatch(p -> p.getIdPerfil().equals(organizadorPerfil.getIdPerfil()));
            if (!jaEhOrganizador) {
                usuarioPerfilDAO.vincularPerfil(
                        evento.getIdOrganizador(), organizadorPerfil.getIdPerfil());
            }
        } else {
            log.warn("Perfil ORGANIZADOR não encontrado no banco de dados. Role não atribuída ao usuário {}.", evento.getIdOrganizador());
        }
    }

    public void atualizar(Long id, Evento evento, Long idOrganizador) {
        if (eventoDAO.buscarPorIdEOrganizador(id, idOrganizador) == null) {
            throw new RecursoNaoEncontradoException();
        }
        evento.setIdEvento(id);
        evento.setIdOrganizador(idOrganizador);
        if (evento.getDtEvento() != null && !evento.getDtEvento().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data do evento deve ser uma data futura.");
        }
        eventoDAO.atualizar(evento);
    }

    public void deletar(Long id, Long idOrganizador) {
        if (eventoDAO.buscarPorIdEOrganizador(id, idOrganizador) == null) {
            throw new RecursoNaoEncontradoException();
        }
        eventoDAO.deletar(id);
    }

    public Integer calcularCapacidadeRestante(Long idEvento) {
        return eventoDAO.calcularCapacidadeRestante(idEvento);
    }

    public List<ResumoVendasResponse> listarResumoVendas(Long idOrganizador) {
        return eventoDAO.listarResumoVendasPorOrganizador(idOrganizador).stream()
                .map(m -> new ResumoVendasResponse(
                        ((Number) m.get("idEvento")).longValue(),
                        (String) m.get("nmEvento"),
                        (LocalDateTime) m.get("dtEvento"),
                        ((Number) m.get("qtCapacidadeTotal")).intValue(),
                        ((Number) m.get("qtIngressosVendidos")).intValue(),
                        ((Number) m.get("qtCapacidadeRestante")).intValue(),
                        m.get("vlReceitaTotal") instanceof BigDecimal bd ? bd
                                : new BigDecimal(m.get("vlReceitaTotal").toString())
                ))
                .toList();
    }
}
