package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.WebhookDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.Webhook;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WebhookDAOImpl implements WebhookDAO {

    private final DataSource dataSource;

    public WebhookDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Webhook webhook) {
        String sql = "INSERT INTO WEB_webhook (web_id_usuario, web_ds_url, web_tp_evento, web_ch_ativo) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, webhook.getIdUsuario());
            stmt.setString(2, webhook.getDsUrl());
            stmt.setString(3, webhook.getTpEvento());
            stmt.setString(4, webhook.getChAtivo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar webhook", e);
        }
    }

    @Override
    public Webhook buscarPorId(Long id) {
        String sql = "SELECT web_id_webhook, web_id_usuario, web_ds_url, web_tp_evento, web_ch_ativo FROM WEB_webhook WHERE web_id_webhook = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar webhook por ID", e);
        }
        return null;
    }

    @Override
    public List<Webhook> listarPorUsuario(Long idUsuario) {
        String sql = "SELECT web_id_webhook, web_id_usuario, web_ds_url, web_tp_evento, web_ch_ativo FROM WEB_webhook WHERE web_id_usuario = ?";
        List<Webhook> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar webhooks do usuário", e);
        }
        return lista;
    }

    @Override
    public List<Webhook> listarAtivosPorTipoEvento(String tipoEvento) {
        String sql = "SELECT web_id_webhook, web_id_usuario, web_ds_url, web_tp_evento, web_ch_ativo FROM WEB_webhook WHERE web_ch_ativo = 'S' AND web_tp_evento = ?";
        List<Webhook> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tipoEvento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar webhooks ativos por tipo de evento", e);
        }
        return lista;
    }

    @Override
    public void atualizar(Webhook webhook) {
        String sql = "UPDATE WEB_webhook SET web_id_usuario=?, web_ds_url=?, web_tp_evento=?, web_ch_ativo=? WHERE web_id_webhook=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, webhook.getIdUsuario());
            stmt.setString(2, webhook.getDsUrl());
            stmt.setString(3, webhook.getTpEvento());
            stmt.setString(4, webhook.getChAtivo());
            stmt.setLong(5, webhook.getIdWebhook());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar webhook", e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM WEB_webhook WHERE web_id_webhook=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao deletar webhook", e);
        }
    }

    private Webhook mapear(ResultSet rs) throws SQLException {
        Webhook obj = new Webhook();
        obj.setIdWebhook(rs.getLong("web_id_webhook"));
        obj.setIdUsuario(rs.getLong("web_id_usuario"));
        obj.setDsUrl(rs.getString("web_ds_url"));
        obj.setTpEvento(rs.getString("web_tp_evento"));
        obj.setChAtivo(rs.getString("web_ch_ativo"));
        return obj;
    }
}
