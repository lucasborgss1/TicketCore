package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.UsuarioPerfil;
import br.com.ticketcore.api.service.UsuarioPerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/usuarios/{idUsuario}/perfis")
public class UsuarioPerfilController {

    private final UsuarioPerfilService usuarioPerfilService;

    public UsuarioPerfilController(UsuarioPerfilService usuarioPerfilService) {
        this.usuarioPerfilService = usuarioPerfilService;
    }

    @Operation(summary = "Listar perfis do usuário", description = "Retorna todos os perfis de acesso vinculados a um usuário")
    @GetMapping
    public ResponseEntity<List<UsuarioPerfil>> listar(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(usuarioPerfilService.buscarPerfisPorUsuario(idUsuario));
    }

    @Operation(summary = "Vincular perfil ao usuário", description = "Associa um perfil de acesso a um usuário")
    @PostMapping("/{idPerfil}")
    public ResponseEntity<Void> vincular(@PathVariable Long idUsuario, @PathVariable Long idPerfil) {
        usuarioPerfilService.vincularPerfil(idUsuario, idPerfil);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
