package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.UsuarioPerfilDAO;
import br.com.ticketcore.api.model.UsuarioPerfil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioPerfilService {

    private final UsuarioPerfilDAO usuarioPerfilDAO;

    public UsuarioPerfilService(UsuarioPerfilDAO usuarioPerfilDAO) {
        this.usuarioPerfilDAO = usuarioPerfilDAO;
    }

    public List<UsuarioPerfil> buscarPerfisPorUsuario(Long idUsuario) {
        return usuarioPerfilDAO.buscarPerfisPorUsuario(idUsuario);
    }

    public void vincularPerfil(Long idUsuario, Long idPerfil) {
        usuarioPerfilDAO.vincularPerfil(idUsuario, idPerfil);
    }

    public void desvincularPerfil(Long idUsuario, Long idPerfil) {
        usuarioPerfilDAO.desvincularPerfil(idUsuario, idPerfil);
    }
}
