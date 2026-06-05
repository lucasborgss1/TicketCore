package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.Categoria;
import br.com.ticketcore.api.model.dto.CategoriaRequest;
import br.com.ticketcore.api.model.dto.CategoriaResponse;
import br.com.ticketcore.api.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(
                categoriaService.listarTodas().stream()
                        .map(CategoriaResponse::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        if (categoria == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(CategoriaResponse.from(categoria));
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNmCategoria(request.nmCategoria());
        categoria.setDsCategoria(request.dsCategoria());
        categoriaService.salvar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id,
                                          @RequestBody CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNmCategoria(request.nmCategoria());
        categoria.setDsCategoria(request.dsCategoria());
        categoriaService.atualizar(id, categoria);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
