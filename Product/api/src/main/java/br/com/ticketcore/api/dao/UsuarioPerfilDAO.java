package br.com.ticketcore.api.dao;

import br.com.ticketcore.api.model.UsuarioPerfil;
import java.util.List;

public interface UsuarioPerfilDAO {
    void vincularPerfil(Long idUsuario, Long idPerfil);
    void desvincularPerfil(Long idUsuario, Long idPerfil);
    List<UsuarioPerfil> buscarPerfisPorUsuario(Long idUsuario);
}