package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    void salvar(Usuario usuario);
    Usuario buscarPorId(Long id);
    Usuario buscarPorEmail(String email);
    List<Usuario> listarTodos();
    void atualizar(Usuario usuario);
    void deletar(Long id);
}