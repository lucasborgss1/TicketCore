package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.IngressoDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.Ingresso;
import br.com.ticketcore.api.model.enums.StatusIngresso;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngressoDAOImpl implements IngressoDAO {

    private final DataSource dataSource;

    public IngressoDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Ingresso ingresso) {
        String sql = "INSERT INTO ING_ingresso (ing_id_transacao, ing_id_tipo_ingresso, ing_cd_acesso, ing_st_ingresso) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ingresso.getIdTransacao());
            stmt.setLong(2, ingresso.getIdTipoIngresso());
            stmt.setString(3, ingresso.getCdAcesso());
            stmt.setString(4, ingresso.getStIngresso().getCodigo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar ingresso", e);
        }
    }

    @Override
    public Ingresso buscarPorId(Long id) {
        String sql = "SELECT ing_id_ingresso, ing_id_transacao, ing_id_tipo_ingresso, ing_cd_acesso, ing_st_ingresso FROM ING_ingresso WHERE ing_id_ingresso = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar ingresso por ID", e);
        }
        return null;
    }

    @Override
    public Ingresso buscarPorIdEComprador(Long idIngresso, Long idComprador) {
        String sql = """
                SELECT i.ing_id_ingresso, i.ing_id_transacao, i.ing_id_tipo_ingresso,
                       i.ing_cd_acesso, i.ing_st_ingresso
                FROM ING_ingresso i
                JOIN TRA_transacao t ON i.ing_id_transacao = t.tra_id_transacao
                WHERE i.ing_id_ingresso = ? AND t.tra_id_comprador = ?
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idIngresso);
            stmt.setLong(2, idComprador);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar ingresso por ID e comprador", e);
        }
        return null;
    }

    @Override
    public Ingresso buscarPorCodigoAcesso(String codigoAcesso) {
        String sql = "SELECT ing_id_ingresso, ing_id_transacao, ing_id_tipo_ingresso, ing_cd_acesso, ing_st_ingresso FROM ING_ingresso WHERE ing_cd_acesso = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoAcesso);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar ingresso por código de acesso", e);
        }
        return null;
    }

    @Override
    public Ingresso buscarPorCodigoEComprador(String codigoAcesso, Long idComprador) {
        String sql = """
                SELECT i.ing_id_ingresso, i.ing_id_transacao, i.ing_id_tipo_ingresso,
                       i.ing_cd_acesso, i.ing_st_ingresso
                FROM ING_ingresso i
                JOIN TRA_transacao t ON i.ing_id_transacao = t.tra_id_transacao
                WHERE i.ing_cd_acesso = ? AND t.tra_id_comprador = ?
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigoAcesso);
            stmt.setLong(2, idComprador);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar ingresso por código e comprador", e);
        }
        return null;
    }

    @Override
    public List<Ingresso> listarPorTransacao(Long idTransacao) {
        String sql = "SELECT ing_id_ingresso, ing_id_transacao, ing_id_tipo_ingresso, ing_cd_acesso, ing_st_ingresso FROM ING_ingresso WHERE ing_id_transacao = ?";
        List<Ingresso> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idTransacao);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar ingressos por transação", e);
        }
        return lista;
    }

    @Override
    public List<Ingresso> listarPorTransacaoEComprador(Long idTransacao, Long idComprador) {
        String sql = """
                SELECT i.ing_id_ingresso, i.ing_id_transacao, i.ing_id_tipo_ingresso,
                       i.ing_cd_acesso, i.ing_st_ingresso
                FROM ING_ingresso i
                JOIN TRA_transacao t ON i.ing_id_transacao = t.tra_id_transacao
                WHERE i.ing_id_transacao = ? AND t.tra_id_comprador = ?
                """;
        List<Ingresso> lista = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idTransacao);
            stmt.setLong(2, idComprador);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar ingressos por transação e comprador", e);
        }
        return lista;
    }

    @Override
    public void atualizar(Ingresso ingresso) {
        String sql = "UPDATE ING_ingresso SET ing_id_transacao=?, ing_id_tipo_ingresso=?, ing_cd_acesso=?, ing_st_ingresso=? WHERE ing_id_ingresso=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ingresso.getIdTransacao());
            stmt.setLong(2, ingresso.getIdTipoIngresso());
            stmt.setString(3, ingresso.getCdAcesso());
            stmt.setString(4, ingresso.getStIngresso().getCodigo());
            stmt.setLong(5, ingresso.getIdIngresso());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar ingresso", e);
        }
    }

    private Ingresso mapear(ResultSet rs) throws SQLException {
        Ingresso obj = new Ingresso();
        obj.setIdIngresso(rs.getLong("ing_id_ingresso"));
        obj.setIdTransacao(rs.getLong("ing_id_transacao"));
        obj.setIdTipoIngresso(rs.getLong("ing_id_tipo_ingresso"));
        obj.setCdAcesso(rs.getString("ing_cd_acesso"));
        obj.setStIngresso(StatusIngresso.fromCodigo(rs.getString("ing_st_ingresso")));
        return obj;
    }
}
