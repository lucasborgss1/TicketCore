package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.NotificacaoDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.Notificacao;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificacaoDAOImpl implements NotificacaoDAO {

    private final DataSource dataSource;

    public NotificacaoDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Notificacao notificacao) {
        String sql = "INSERT INTO NOT_notificacao (not_id_usuario, not_ds_mensagem) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, notificacao.getIdUsuario());
            stmt.setString(2, notificacao.getDsMensagem());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar notificação", e);
        }
    }

    @Override
    public List<Notificacao> listarPorUsuario(Long idUsuario) {
        String sql = """
                SELECT not_id_notificacao, not_id_usuario, not_ds_mensagem, not_ch_lida, not_dt_criacao
                FROM NOT_notificacao
                WHERE not_id_usuario = ?
                ORDER BY not_dt_criacao DESC
                """;
        List<Notificacao> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao listar notificações do usuário", e);
        }
        return lista;
    }

    @Override
    public void marcarComoLida(Long id) {
        String sql = "UPDATE NOT_notificacao SET not_ch_lida = 'S' WHERE not_id_notificacao = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro ao marcar notificação como lida", e);
        }
    }

    private Notificacao mapear(ResultSet rs) throws SQLException {
        Notificacao obj = new Notificacao();
        obj.setIdNotificacao(rs.getLong("not_id_notificacao"));
        obj.setIdUsuario(rs.getLong("not_id_usuario"));
        obj.setDsMensagem(rs.getString("not_ds_mensagem"));
        obj.setChLida(rs.getString("not_ch_lida"));
        Timestamp ts = rs.getTimestamp("not_dt_criacao");
        if (ts != null) {
            obj.setDtCriacao(ts.toLocalDateTime());
        }
        return obj;
    }
}