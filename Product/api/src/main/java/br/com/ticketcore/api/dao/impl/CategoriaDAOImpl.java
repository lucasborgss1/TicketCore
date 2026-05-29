package br.com.ticketcore.api.dao.impl;

import br.com.ticketcore.api.dao.CategoriaDAO;
import br.com.ticketcore.api.exception.DAOException;
import br.com.ticketcore.api.model.Categoria;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoriaDAOImpl implements CategoriaDAO {

    private final DataSource dataSource;

    public CategoriaDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvar(Categoria categoria) {
        String sql = "INSERT INTO CAT_categoria (cat_nm_categoria, cat_ds_categoria) VALUES (?, ?)";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNmCategoria());
            stmt.setString(2, categoria.getDsCategoria());

            stmt.executeUpdate();

        } catch (SQLException e){
            throw new DAOException("Erro ao salvar categoria", e);
        }
    }

    @Override
    public Categoria buscarPorId(Long id) {
        String sql = "SELECT cat_id_categoria, cat_nm_categoria, cat_ds_categoria FROM CAT_categoria WHERE cat_id_categoria = ?";
        Categoria categoria = null;

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    categoria = mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar categoria", e);
        }

        return categoria;
    }

    @Override
    public List<Categoria> listarTodas() {
        String sql = "SELECT cat_id_categoria, cat_nm_categoria, cat_ds_categoria FROM CAT_categoria ORDER BY cat_nm_categoria";
        List<Categoria> categorias = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()){
                categorias.add(mapear(rs));
            }
        }
        catch (SQLException e) {
            throw new DAOException("Erro ao listar categorias", e);
        }
        return categorias;
    }

    @Override
    public void atualizar(Categoria categoria) {
        String sql = "UPDATE CAT_categoria SET cat_nm_categoria=?, cat_ds_categoria=? WHERE cat_id_categoria=?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNmCategoria());
            stmt.setString(2, categoria.getDsCategoria());
            stmt.setLong(3, categoria.getIdCategoria());

            stmt.executeUpdate();

        } catch (SQLException e){
            throw new DAOException("Erro ao atualizar a categoria", e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM CAT_categoria WHERE cat_id_categoria=?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e){
            throw new DAOException("Erro ao excluir a categoria", e);
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria obj = new Categoria();

        obj.setIdCategoria(rs.getLong("cat_id_categoria"));
        obj.setNmCategoria(rs.getString("cat_nm_categoria"));
        obj.setDsCategoria(rs.getString("cat_ds_categoria"));
        return obj;
    }
}