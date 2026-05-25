package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.TipoIngresso;
import java.util.List;

public interface TipoIngressoDAO {
    void salvar(TipoIngresso tipoIngresso);
    TipoIngresso buscarPorId(Long id);
    List<TipoIngresso> listarPorEvento(Long idEvento);
    void atualizar(TipoIngresso tipoIngresso);
    void deletar(Long id);
}