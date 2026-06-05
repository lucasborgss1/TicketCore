package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.PerfilResponse;
import br.com.ticketcore.api.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Perfis", description = "Consulta de perfis de acesso disponíveis no sistema")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @Operation(summary = "Listar perfis", description = "Retorna todos os perfis de acesso cadastrados (ex: ADMIN, ORGANIZADOR, COMPRADOR)")
    @GetMapping
    public ResponseEntity<List<PerfilResponse>> listar() {
        return ResponseEntity.ok(
                perfilService.listarTodos().stream()
                        .map(PerfilResponse::from)
                        .toList()
        );
    }
}
