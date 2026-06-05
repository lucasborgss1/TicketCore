package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.UsuarioPerfil;
import br.com.ticketcore.api.service.UsuarioPerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/perfis")
public class UsuarioPerfilController {

    private final UsuarioPerfilService usuarioPerfilService;

    public UsuarioPerfilController(UsuarioPerfilService usuarioPerfilService) {
        this.usuarioPerfilService = usuarioPerfilService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioPerfil>> listar(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(usuarioPerfilService.buscarPerfisPorUsuario(idUsuario));
    }

    @PostMapping("/{idPerfil}")
    public ResponseEntity<Void> vincular(@PathVariable Long idUsuario, @PathVariable Long idPerfil) {
        usuarioPerfilService.vincularPerfil(idUsuario, idPerfil);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}