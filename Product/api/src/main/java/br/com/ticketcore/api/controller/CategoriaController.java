package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.Categoria;
import br.com.ticketcore.api.model.dto.CategoriaResponse;
import br.com.ticketcore.api.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "Consulta de categorias de eventos")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias cadastradas")
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(
                categoriaService.listarTodas().stream()
                        .map(CategoriaResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Buscar por ID", description = "Retorna uma categoria pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        if (categoria == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(CategoriaResponse.from(categoria));
    }
}
