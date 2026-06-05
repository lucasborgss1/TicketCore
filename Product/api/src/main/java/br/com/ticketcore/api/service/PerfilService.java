package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.PerfilDAO;
import br.com.ticketcore.api.model.Perfil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilDAO perfilDAO;

    public PerfilService(PerfilDAO perfilDAO) {
        this.perfilDAO = perfilDAO;
    }

    public List<Perfil> listarTodos() {
        return perfilDAO.listarTodos();
    }

    public Perfil buscarPorId(Long id) {
        return perfilDAO.buscarPorId(id);
    }

    public void salvar(Perfil perfil) {
        perfilDAO.salvar(perfil);
    }

    public void atualizar(Long id, Perfil perfil) {
        perfil.setIdPerfil(id);
        perfilDAO.atualizar(perfil);
    }

    public void deletar(Long id) {
        perfilDAO.deletar(id);
    }
}
