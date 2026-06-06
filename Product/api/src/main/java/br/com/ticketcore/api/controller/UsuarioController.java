package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.Usuario;
import br.com.ticketcore.api.model.dto.UsuarioAtualizacaoRequest;
import br.com.ticketcore.api.model.dto.UsuarioCadastroRequest;
import br.com.ticketcore.api.model.dto.UsuarioResponse;
import br.com.ticketcore.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Buscar por ID", description = "Retorna os dados de um usuário pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UsuarioResponse.from(usuario));
    }

    @Operation(summary = "Buscar por e-mail", description = "Retorna os dados de um usuário pelo e-mail")
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UsuarioResponse.from(usuario));
    }

    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário. Endpoint público — não requer token.")
    @PostMapping
    public ResponseEntity<UsuarioResponse> salvar(@Valid @RequestBody UsuarioCadastroRequest request) {
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

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente. Usuário só pode atualizar seus próprios dados.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id,
                                          @Valid @RequestBody UsuarioAtualizacaoRequest request,
                                          @AuthenticationPrincipal Long idAutenticado) {
        if (!id.equals(idAutenticado)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Usuario usuario = new Usuario();
        usuario.setNmUsuario(request.nmUsuario());
        usuario.setDsEmail(request.dsEmail());
        usuario.setDsSenha(request.dsSenha());
        usuario.setDsFotoPerfil(request.dsFotoPerfil());
        usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remover usuário", description = "Remove um usuário pelo ID. Usuário só pode remover seu próprio cadastro.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id,
                                        @AuthenticationPrincipal Long idAutenticado) {
        if (!id.equals(idAutenticado)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
