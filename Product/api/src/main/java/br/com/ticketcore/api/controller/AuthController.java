package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.config.JwtUtil;
import br.com.ticketcore.api.model.Usuario;
import br.com.ticketcore.api.model.dto.LoginRequest;
import br.com.ticketcore.api.model.dto.LoginResponse;
import br.com.ticketcore.api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints de autenticação")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UsuarioService usuarioService,
                          JwtUtil jwtUtil,
                          BCryptPasswordEncoder encoder) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    @Operation(summary = "Realiza login", description = "Autentica o usuário e retorna um token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(request.dsEmail());
        if (usuario == null || !encoder.matches(request.dsSenha(), usuario.getDsSenha())) {
            return ResponseEntity.status(401).build();
        }
        String token = jwtUtil.gerarToken(usuario.getIdUsuario(), usuario.getDsEmail());
        return ResponseEntity.ok(new LoginResponse(token, usuario.getIdUsuario(), usuario.getNmUsuario()));
    }
}
