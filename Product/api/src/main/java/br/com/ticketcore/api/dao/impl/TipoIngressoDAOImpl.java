package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.TipoIngressoDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.TipoIngresso;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TipoIngressoDAOImpl implements TipoIngressoDAO {

    private final DataSource dataSource;

    public TipoIngressoDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(TipoIngresso tipoIngresso) {
        String sql = "INSERT INTO TPI_tipo_ingresso (tpi_id_evento, tpi_nm_tipo, tpi_vl_preco, tpi_qt_lote) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, tipoIngresso.getIdEvento());
            stmt.setString(2, tipoIngresso.getNmTipo());
            stmt.setBigDecimal(3, tipoIngresso.getVlPreco());
            stmt.setInt(4, tipoIngresso.getQtLote());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar tipo de ingresso", e);
        }
    }

    @Override
    public TipoIngresso buscarPorId(Long id) {
        String sql = "SELECT tpi_id_tipo_ingresso, tpi_id_evento, tpi_nm_tipo, tpi_vl_preco, tpi_qt_lote FROM TPI_tipo_ingresso WHERE tpi_id_tipo_ingresso = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar tipo de ingresso por ID", e);
        }
        return null;
    }

    @Override
    public List<TipoIngresso> listarPorEvento(Long idEvento) {
        String sql = "SELECT tpi_id_tipo_ingresso, tpi_id_evento, tpi_nm_tipo, tpi_vl_preco, tpi_qt_lote FROM TPI_tipo_ingresso WHERE tpi_id_evento = ? ORDER BY tpi_nm_tipo";
        List<TipoIngresso> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar tipos de ingresso por evento", e);
        }
        return lista;
    }

    @Override
    public void atualizar(TipoIngresso tipoIngresso) {
        String sql = "UPDATE TPI_tipo_ingresso SET tpi_id_evento=?, tpi_nm_tipo=?, tpi_vl_preco=?, tpi_qt_lote=? WHERE tpi_id_tipo_ingresso=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, tipoIngresso.getIdEvento());
            stmt.setString(2, tipoIngresso.getNmTipo());
            stmt.setBigDecimal(3, tipoIngresso.getVlPreco());
            stmt.setInt(4, tipoIngresso.getQtLote());
            stmt.setLong(5, tipoIngresso.getIdTipoIngresso());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar tipo de ingresso", e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM TPI_tipo_ingresso WHERE tpi_id_tipo_ingresso=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao deletar tipo de ingresso", e);
        }
    }

    private TipoIngresso mapear(ResultSet rs) throws SQLException {
        TipoIngresso obj = new TipoIngresso();
        obj.setIdTipoIngresso(rs.getLong("tpi_id_tipo_ingresso"));
        obj.setIdEvento(rs.getLong("tpi_id_evento"));
        obj.setNmTipo(rs.getString("tpi_nm_tipo"));
        obj.setVlPreco(rs.getBigDecimal("tpi_vl_preco"));
        obj.setQtLote(rs.getInt("tpi_qt_lote"));
        return obj;
    }
}
