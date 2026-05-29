package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.UsuarioPerfilDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.UsuarioPerfil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioPerfilDAOImpl implements UsuarioPerfilDAO {

    private final DataSource dataSource;

    public UsuarioPerfilDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void vincularPerfil(Long idUsuario, Long idPerfil) {
        String sql = "INSERT INTO USP_usuario_perfil (usp_id_usuario, usp_id_perfil) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            stmt.setLong(2, idPerfil);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao vincular perfil ao usuário", e);
        }
    }

    @Override
    public void desvincularPerfil(Long idUsuario, Long idPerfil) {
        String sql = "DELETE FROM USP_usuario_perfil WHERE usp_id_usuario=? AND usp_id_perfil=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            stmt.setLong(2, idPerfil);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao desvincular perfil do usuário", e);
        }
    }

    @Override
    public List<UsuarioPerfil> buscarPerfisPorUsuario(Long idUsuario) {
        String sql = "SELECT usp_id_usuario, usp_id_perfil FROM USP_usuario_perfil WHERE usp_id_usuario = ?";
        List<UsuarioPerfil> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar perfis do usuário", e);
        }
        return lista;
    }

    private UsuarioPerfil mapear(ResultSet rs) throws SQLException {
        UsuarioPerfil obj = new UsuarioPerfil();
        obj.setIdUsuario(rs.getLong("usp_id_usuario"));
        obj.setIdPerfil(rs.getLong("usp_id_perfil"));
        return obj;
    }
}
