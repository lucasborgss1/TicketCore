package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.EventoDAO;
import br.com.ticketcore.api.model.Evento;
import br.com.ticketcore.api.model.dto.ResumoVendasResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EventoService {

    private final EventoDAO eventoDAO;

    public EventoService(EventoDAO eventoDAO) {
        this.eventoDAO = eventoDAO;
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
        eventoDAO.salvar(evento);
    }

    public void atualizar(Long id, Evento evento) {
        evento.setIdEvento(id);
        eventoDAO.atualizar(evento);
    }

    public void deletar(Long id) {
        eventoDAO.deletar(id);
    }

    public Integer calcularCapacidadeRestante(Long idEvento) {
        return eventoDAO.calcularCapacidadeRestante(idEvento);
    }

    public List<ResumoVendasResponse> listarResumoVendas() {
        return eventoDAO.listarResumoVendas().stream()
                .map(m -> new ResumoVendasResponse(
                        (Long) m.get("idEvento"),
                        (String) m.get("nmEvento"),
                        (LocalDateTime) m.get("dtEvento"),
                        (Integer) m.get("qtCapacidadeTotal"),
                        (Integer) m.get("qtIngressosVendidos"),
                        (Integer) m.get("qtCapacidadeRestante"),
                        (BigDecimal) m.get("vlReceitaTotal")
                ))
                .toList();
    }
}