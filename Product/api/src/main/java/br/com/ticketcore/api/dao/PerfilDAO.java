package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.Perfil;
import java.util.List;

public interface PerfilDAO {
    void salvar(Perfil perfil);
    Perfil buscarPorId(Long id);
    List<Perfil> listarTodos();
    void atualizar(Perfil perfil);
    void deletar(Long id);
}