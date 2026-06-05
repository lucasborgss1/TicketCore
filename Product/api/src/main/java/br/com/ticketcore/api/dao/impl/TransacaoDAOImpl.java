package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.TransacaoDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.Transacao;
import br.com.ticketcore.api.model.enums.StatusTransacao;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransacaoDAOImpl implements TransacaoDAO {

    private final DataSource dataSource;

    public TransacaoDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long salvar(Transacao transacao) {
        String sql = "INSERT INTO TRA_transacao (tra_id_comprador, tra_st_transacao, tra_vl_total) VALUES (?, ?, ?) RETURNING tra_id_transacao";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, transacao.getIdComprador());
            stmt.setString(2, transacao.getStTransacao().getCodigo());
            stmt.setBigDecimal(3, transacao.getVlTotal());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar transação", e);
        }
        throw new DAOException("Nenhum ID retornado ao salvar transação", null);
    }

    @Override
    public Transacao buscarPorId(Long id) {
        String sql = "SELECT tra_id_transacao, tra_id_comprador, tra_dt_transacao, tra_st_transacao, tra_vl_total FROM TRA_transacao WHERE tra_id_transacao = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar transação por ID", e);
        }
        return null;
    }

    @Override
    public List<Transacao> listarPorComprador(Long idComprador) {
        String sql = "SELECT tra_id_transacao, tra_id_comprador, tra_dt_transacao, tra_st_transacao, tra_vl_total FROM TRA_transacao WHERE tra_id_comprador = ? ORDER BY tra_dt_transacao DESC";
        List<Transacao> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idComprador);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar transações do comprador", e);
        }
        return lista;
    }

    @Override
    public void atualizar(Transacao transacao) {
        String sql = "UPDATE TRA_transacao SET tra_id_comprador=?, tra_st_transacao=?, tra_vl_total=? WHERE tra_id_transacao=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, transacao.getIdComprador());
            stmt.setString(2, transacao.getStTransacao().getCodigo());
            stmt.setBigDecimal(3, transacao.getVlTotal());
            stmt.setLong(4, transacao.getIdTransacao());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar transação", e);
        }
    }

    @Override
    public void atualizarStatus(Long idTransacao, String status) {
        String sql = "UPDATE TRA_transacao SET tra_st_transacao = ? WHERE tra_id_transacao = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, idTransacao);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar status da transação", e);
        }
    }

    @Override
    public void cancelarTransacao(Long idTransacao, Long idUsuarioSolicitante) {
        String sql = "CALL PR_cancelar_transacao(?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idTransacao);
            stmt.setLong(2, idUsuarioSolicitante);
            stmt.execute();
        } catch (SQLException e) {
            // A procedure lança RAISE EXCEPTION com mensagens de negócio
            throw new DAOException(e.getMessage(), e);
        }
    }

    private Transacao mapear(ResultSet rs) throws SQLException {
        Transacao obj = new Transacao();
        obj.setIdTransacao(rs.getLong("tra_id_transacao"));
        obj.setIdComprador(rs.getLong("tra_id_comprador"));
        Timestamp ts = rs.getTimestamp("tra_dt_transacao");
        if (ts != null) {
            obj.setDtTransacao(ts.toLocalDateTime());
        }
        obj.setStTransacao(StatusTransacao.fromCodigo(rs.getString("tra_st_transacao")));
        obj.setVlTotal(rs.getBigDecimal("tra_vl_total"));
        return obj;
    }
}
