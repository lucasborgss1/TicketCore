package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.EventoDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.Evento;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventoDAOImpl implements EventoDAO {

    private final DataSource dataSource;

    public EventoDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Evento evento) {
        String sql = "INSERT INTO EVE_evento (eve_id_organizador, eve_id_categoria, eve_nm_evento, eve_dt_evento, eve_nm_local, eve_qt_capacidade, eve_ds_imagem) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, evento.getIdOrganizador());
            stmt.setLong(2, evento.getIdCategoria());
            stmt.setString(3, evento.getNmEvento());
            stmt.setTimestamp(4, Timestamp.valueOf(evento.getDtEvento()));
            stmt.setString(5, evento.getNmLocal());
            stmt.setInt(6, evento.getQtCapacidade());
            stmt.setString(7, evento.getDsImagem());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar evento", e);
        }
    }

    @Override
    public Evento buscarPorId(Long id) {
        String sql = "SELECT eve_id_evento, eve_id_organizador, eve_id_categoria, eve_nm_evento, eve_dt_evento, eve_nm_local, eve_qt_capacidade, eve_ds_imagem FROM EVE_evento WHERE eve_id_evento = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar evento por ID", e);
        }
        return null;
    }

    @Override
    public List<Evento> listarTodos() {
        String sql = "SELECT eve_id_evento, eve_id_organizador, eve_id_categoria, eve_nm_evento, eve_dt_evento, eve_nm_local, eve_qt_capacidade, eve_ds_imagem FROM EVE_evento ORDER BY eve_dt_evento";
        List<Evento> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar eventos", e);
        }
        return lista;
    }

    @Override
    public List<Evento> listarPorOrganizador(Long idOrganizador) {
        String sql = "SELECT eve_id_evento, eve_id_organizador, eve_id_categoria, eve_nm_evento, eve_dt_evento, eve_nm_local, eve_qt_capacidade, eve_ds_imagem FROM EVE_evento WHERE eve_id_organizador = ? ORDER BY eve_dt_evento";
        List<Evento> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idOrganizador);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar eventos por organizador", e);
        }
        return lista;
    }

    @Override
    public void atualizar(Evento evento) {
        String sql = "UPDATE EVE_evento SET eve_id_organizador=?, eve_id_categoria=?, eve_nm_evento=?, eve_dt_evento=?, eve_nm_local=?, eve_qt_capacidade=?, eve_ds_imagem=? WHERE eve_id_evento=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, evento.getIdOrganizador());
            stmt.setLong(2, evento.getIdCategoria());
            stmt.setString(3, evento.getNmEvento());
            stmt.setTimestamp(4, Timestamp.valueOf(evento.getDtEvento()));
            stmt.setString(5, evento.getNmLocal());
            stmt.setInt(6, evento.getQtCapacidade());
            stmt.setString(7, evento.getDsImagem());
            stmt.setLong(8, evento.getIdEvento());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar evento", e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM EVE_evento WHERE eve_id_evento=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao deletar evento", e);
        }
    }

    @Override
    public Integer calcularCapacidadeRestante(Long idEvento) {
        String sql = "SELECT FN_calcular_capacidade_restante(?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao calcular capacidade restante do evento", e);
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> listarResumoVendas() {
        String sql = "SELECT eve_id_evento, eve_nm_evento, eve_dt_evento, qt_capacidade_total, qt_ingressos_vendidos, qt_capacidade_restante, vl_receita_total FROM VW_resumo_vendas_evento ORDER BY eve_dt_evento";
        List<Map<String, Object>> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("idEvento", rs.getLong("eve_id_evento"));
                linha.put("nmEvento", rs.getString("eve_nm_evento"));
                linha.put("dtEvento", rs.getTimestamp("eve_dt_evento").toLocalDateTime());
                linha.put("qtCapacidadeTotal", rs.getInt("qt_capacidade_total"));
                linha.put("qtIngressosVendidos", rs.getInt("qt_ingressos_vendidos"));
                linha.put("qtCapacidadeRestante", rs.getInt("qt_capacidade_restante"));
                linha.put("vlReceitaTotal", rs.getBigDecimal("vl_receita_total"));
                lista.add(linha);
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar resumo de vendas por evento", e);
        }
        return lista;
    }

    @Override
    public Long buscarOrganizadorPorTipoIngresso(Long idTipoIngresso) {
        String sql = """
                SELECT e.eve_id_organizador
                FROM EVE_evento e
                JOIN TPI_tipo_ingresso tpi ON tpi.tpi_id_evento = e.eve_id_evento
                WHERE tpi.tpi_id_tipo_ingresso = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idTipoIngresso);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar organizador por tipo de ingresso", e);
        }
        return null;
    }

    @Override
    public Long buscarOrganizadorPorTransacao(Long idTransacao) {
        String sql = """
                SELECT DISTINCT e.eve_id_organizador
                FROM EVE_evento e
                JOIN TPI_tipo_ingresso tpi ON tpi.tpi_id_evento = e.eve_id_evento
                JOIN ING_ingresso ing ON ing.ing_id_tipo_ingresso = tpi.tpi_id_tipo_ingresso
                WHERE ing.ing_id_transacao = ?
                LIMIT 1
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idTransacao);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar organizador por transação", e);
        }
        return null;
    }

    private Evento mapear(ResultSet rs) throws SQLException {
        Evento obj = new Evento();
        obj.setIdEvento(rs.getLong("eve_id_evento"));
        obj.setIdOrganizador(rs.getLong("eve_id_organizador"));
        obj.setIdCategoria(rs.getLong("eve_id_categoria"));
        obj.setNmEvento(rs.getString("eve_nm_evento"));
        obj.setDtEvento(rs.getTimestamp("eve_dt_evento").toLocalDateTime());
        obj.setNmLocal(rs.getString("eve_nm_local"));
        obj.setQtCapacidade(rs.getInt("eve_qt_capacidade"));
        obj.setDsImagem(rs.getString("eve_ds_imagem"));
        return obj;
    }
}
