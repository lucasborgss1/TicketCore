package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.Categoria;
import br.com.ticketcore.api.model.dto.CategoriaRequest;
import br.com.ticketcore.api.model.dto.CategoriaResponse;
import br.com.ticketcore.api.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "Gerenciamento de categorias de eventos")
@SecurityRequirement(name = "bearerAuth")
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

    @Operation(summary = "Cadastrar categoria", description = "Cria uma nova categoria de evento")
    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNmCategoria(request.nmCategoria());
        categoria.setDsCategoria(request.dsCategoria());
        categoriaService.salvar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id,
                                          @RequestBody CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNmCategoria(request.nmCategoria());
        categoria.setDsCategoria(request.dsCategoria());
        categoriaService.atualizar(id, categoria);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remover categoria", description = "Remove uma categoria pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
