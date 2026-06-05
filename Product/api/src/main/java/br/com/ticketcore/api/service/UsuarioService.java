package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.UsuarioDAO;
import br.com.ticketcore.api.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(UsuarioDAO usuarioDAO, BCryptPasswordEncoder encoder) {
        this.usuarioDAO = usuarioDAO;
        this.encoder = encoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioDAO.buscarPorEmail(email);
    }

    public void salvar(Usuario usuario) {
        usuario.setDsSenha(encoder.encode(usuario.getDsSenha()));
        usuarioDAO.salvar(usuario);
    }

    public void atualizar(Long id, Usuario usuario) {
        if (usuario.getDsSenha() != null && !usuario.getDsSenha().isBlank()) {
            usuario.setDsSenha(encoder.encode(usuario.getDsSenha()));
        } else {
            Usuario atual = usuarioDAO.buscarPorId(id);
            if (atual == null) {
                throw new IllegalArgumentException("Usuário não encontrado com id: " + id);
            }
            usuario.setDsSenha(atual.getDsSenha());
        }
        usuario.setIdUsuario(id);
        usuarioDAO.atualizar(usuario);
    }

    public void deletar(Long id) {
        usuarioDAO.deletar(id);
    }
}
