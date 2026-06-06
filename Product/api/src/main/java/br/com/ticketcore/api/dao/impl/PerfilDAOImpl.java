package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.PerfilDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.Perfil;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PerfilDAOImpl implements PerfilDAO {

    private final DataSource dataSource;

    public PerfilDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Perfil perfil) {
        String sql = "INSERT INTO PER_perfil (per_nm_perfil) VALUES (?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, perfil.getNmPerfil());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar perfil", e);
        }
    }

    @Override
    public Perfil buscarPorId(Long id) {
        String sql = "SELECT per_id_perfil, per_nm_perfil FROM PER_perfil WHERE per_id_perfil = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar perfil por ID", e);
        }
        return null;
    }

    @Override
    public List<Perfil> listarTodos() {
        String sql = "SELECT per_id_perfil, per_nm_perfil FROM PER_perfil ORDER BY per_nm_perfil";
        List<Perfil> perfis = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                perfis.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar perfis", e);
        }
        return perfis;
    }

    @Override
    public void atualizar(Perfil perfil) {
        String sql = "UPDATE PER_perfil SET per_nm_perfil=? WHERE per_id_perfil=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, perfil.getNmPerfil());
            stmt.setLong(2, perfil.getIdPerfil());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar perfil", e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM PER_perfil WHERE per_id_perfil=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao deletar perfil", e);
        }
    }

    @Override
    public Perfil buscarPorNome(String nome) {
        String sql = "SELECT per_id_perfil, per_nm_perfil FROM PER_perfil WHERE per_nm_perfil = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar perfil por nome", e);
        }
        return null;
    }

    @Override
    public List<String> buscarNomesPorUsuario(Long idUsuario) {
        String sql = """
                SELECT p.per_nm_perfil
                FROM PER_perfil p
                JOIN USP_usuario_perfil usp ON usp.usp_id_perfil = p.per_id_perfil
                WHERE usp.usp_id_usuario = ?
                """;
        List<String> nomes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    nomes.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar nomes de perfis por usuário", e);
        }
        return nomes;
    }

    private Perfil mapear(ResultSet rs) throws SQLException {
        Perfil obj = new Perfil();
        obj.setIdPerfil(rs.getLong("per_id_perfil"));
        obj.setNmPerfil(rs.getString("per_nm_perfil"));
        return obj;
    }
}
