package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.CategoriaDAO;
import br.com.ticketcore.api.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public List<Categoria> listarTodas() {
        return categoriaDAO.listarTodas();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaDAO.buscarPorId(id);
    }

    public void salvar(Categoria categoria) {
        categoriaDAO.salvar(categoria);
    }

    public void atualizar(Long id, Categoria categoria) {
        categoria.setIdCategoria(id);
        categoriaDAO.atualizar(categoria);
    }

    public void deletar(Long id) {
        categoriaDAO.deletar(id);
    }
}
