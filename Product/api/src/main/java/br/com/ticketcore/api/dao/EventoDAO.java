package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Evento;
import java.util.List;
import java.util.Map;

public interface EventoDAO {
    void salvar(Evento evento);
    Evento buscarPorId(Long id);
    List<Evento> listarTodos();
    List<Evento> listarPorOrganizador(Long idOrganizador);
    void atualizar(Evento evento);
    void deletar(Long id);

    Integer calcularCapacidadeRestante(Long idEvento); // Chama a Stored Function

    Evento buscarPorIdEOrganizador(Long idEvento, Long idOrganizador);
    List<Map<String, Object>> listarResumoVendasPorOrganizador(Long idOrganizador);

    Long buscarOrganizadorPorTipoIngresso(Long idTipoIngresso);
    Long buscarOrganizadorPorTransacao(Long idTransacao);
}