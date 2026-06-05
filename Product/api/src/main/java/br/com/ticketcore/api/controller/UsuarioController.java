package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.Usuario;
import br.com.ticketcore.api.model.dto.UsuarioCadastroRequest;
import br.com.ticketcore.api.model.dto.UsuarioAtualizacaoRequest;
import br.com.ticketcore.api.model.dto.UsuarioResponse;
import br.com.ticketcore.api.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UsuarioResponse.from(usuario));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UsuarioResponse.from(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@RequestBody UsuarioCadastroRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNmUsuario(request.nmUsuario());
        usuario.setDsEmail(request.dsEmail());
        usuario.setDsSenha(request.dsSenha());
        usuario.setNuCpfCnpj(request.nuCpfCnpj());
        usuario.setDsFotoPerfil(request.dsFotoPerfil());
        usuarioService.salvar(usuario);
        Usuario salvo = usuarioService.buscarPorEmail(request.dsEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id,
                                          @RequestBody UsuarioAtualizacaoRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNmUsuario(request.nmUsuario());
        usuario.setDsEmail(request.dsEmail());
        usuario.setDsSenha(request.dsSenha());
        usuario.setDsFotoPerfil(request.dsFotoPerfil());
        usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
