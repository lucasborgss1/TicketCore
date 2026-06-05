package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Categoria;
import java.util.List;

public interface CategoriaDAO {
    void salvar(Categoria categoria);
    Categoria buscarPorId(Long id);
    List<Categoria> listarTodas();
    void atualizar(Categoria categoria);
    void deletar(Long id);
}