package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.UsuarioDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.Usuario;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDAOImpl implements UsuarioDAO {

    private final DataSource dataSource;

    public UsuarioDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO USU_usuario (usu_nm_usuario, usu_ds_email, usu_ds_senha, usu_nu_cpf_cnpj, usu_ds_foto_perfil) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNmUsuario());
            stmt.setString(2, usuario.getDsEmail());
            stmt.setString(3, usuario.getDsSenha());
            stmt.setString(4, usuario.getNuCpfCnpj());
            stmt.setString(5, usuario.getDsFotoPerfil());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar usuário", e);
        }
    }

    @Override
    public Usuario buscarPorId(Long id) {
        String sql = "SELECT usu_id_usuario, usu_nm_usuario, usu_ds_email, usu_ds_senha, usu_nu_cpf_cnpj, usu_ds_foto_perfil, usu_dt_cadastro FROM USU_usuario WHERE usu_id_usuario = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário por ID", e);
        }
        return null;
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT usu_id_usuario, usu_nm_usuario, usu_ds_email, usu_ds_senha, usu_nu_cpf_cnpj, usu_ds_foto_perfil, usu_dt_cadastro FROM USU_usuario WHERE usu_ds_email = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário por e-mail", e);
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT usu_id_usuario, usu_nm_usuario, usu_ds_email, usu_ds_senha, usu_nu_cpf_cnpj, usu_ds_foto_perfil, usu_dt_cadastro FROM USU_usuario ORDER BY usu_nm_usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar usuários", e);
        }
        return usuarios;
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE USU_usuario SET usu_nm_usuario=?, usu_ds_email=?, usu_ds_senha=?, usu_nu_cpf_cnpj=?, usu_ds_foto_perfil=? WHERE usu_id_usuario=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNmUsuario());
            stmt.setString(2, usuario.getDsEmail());
            stmt.setString(3, usuario.getDsSenha());
            stmt.setString(4, usuario.getNuCpfCnpj());
            stmt.setString(5, usuario.getDsFotoPerfil());
            stmt.setLong(6, usuario.getIdUsuario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar usuário", e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM USU_usuario WHERE usu_id_usuario=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao deletar usuário", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario obj = new Usuario();
        obj.setIdUsuario(rs.getLong("usu_id_usuario"));
        obj.setNmUsuario(rs.getString("usu_nm_usuario"));
        obj.setDsEmail(rs.getString("usu_ds_email"));
        obj.setDsSenha(rs.getString("usu_ds_senha"));
        obj.setNuCpfCnpj(rs.getString("usu_nu_cpf_cnpj"));
        obj.setDsFotoPerfil(rs.getString("usu_ds_foto_perfil"));
        Timestamp ts = rs.getTimestamp("usu_dt_cadastro");
        if (ts != null) {
            obj.setDtCadastro(ts.toLocalDateTime());
        }
        return obj;
    }}