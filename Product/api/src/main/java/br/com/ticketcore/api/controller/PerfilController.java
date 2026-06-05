package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.PerfilResponse;
import br.com.ticketcore.api.service.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public ResponseEntity<List<PerfilResponse>> listar() {
        return ResponseEntity.ok(
                perfilService.listarTodos().stream()
                        .map(PerfilResponse::from)
                        .toList()
        );
    }
}