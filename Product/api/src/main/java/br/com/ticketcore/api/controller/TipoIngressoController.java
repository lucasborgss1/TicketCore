package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.TipoIngresso;
import br.com.ticketcore.api.model.dto.TipoIngressoRequest;
import br.com.ticketcore.api.model.dto.TipoIngressoResponse;
import br.com.ticketcore.api.service.TipoIngressoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tipos de Ingresso", description = "Gerenciamento dos tipos de ingresso por evento (ex: Pista, VIP)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/tipos-ingresso")
public class TipoIngressoController {

    private final TipoIngressoService tipoIngressoService;

    public TipoIngressoController(TipoIngressoService tipoIngressoService) {
        this.tipoIngressoService = tipoIngressoService;
    }

    @Operation(summary = "Buscar por ID", description = "Retorna um tipo de ingresso pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<TipoIngressoResponse> buscarPorId(@PathVariable Long id) {
        TipoIngresso tipo = tipoIngressoService.buscarPorId(id);
        if (tipo == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(TipoIngressoResponse.from(tipo));
    }

    @Operation(summary = "Listar por evento", description = "Retorna todos os tipos de ingresso de um evento")
    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<TipoIngressoResponse>> listarPorEvento(@PathVariable Long idEvento) {
        return ResponseEntity.ok(
                tipoIngressoService.listarPorEvento(idEvento).stream()
                        .map(TipoIngressoResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Cadastrar tipo de ingresso", description = "Cria um novo tipo de ingresso para um evento do organizador autenticado")
    @PreAuthorize("hasRole('ORGANIZADOR')")
    @PostMapping
    public ResponseEntity<Void> salvar(
            @Valid @RequestBody TipoIngressoRequest request,
            @AuthenticationPrincipal Long idUsuario) {
        TipoIngresso tipo = new TipoIngresso();
        tipo.setIdEvento(request.idEvento());
        tipo.setNmTipo(request.nmTipo());
        tipo.setVlPreco(request.vlPreco());
        tipo.setQtLote(request.qtLote());
        tipoIngressoService.salvar(tipo, idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualizar tipo de ingresso", description = "Atualiza os dados de um tipo de ingresso de evento do organizador autenticado")
    @PreAuthorize("hasRole('ORGANIZADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoIngressoRequest request,
            @AuthenticationPrincipal Long idUsuario) {
        TipoIngresso tipo = new TipoIngresso();
        tipo.setIdEvento(request.idEvento());
        tipo.setNmTipo(request.nmTipo());
        tipo.setVlPreco(request.vlPreco());
        tipo.setQtLote(request.qtLote());
        tipoIngressoService.atualizar(id, tipo, idUsuario);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remover tipo de ingresso", description = "Remove um tipo de ingresso de evento do organizador autenticado")
    @PreAuthorize("hasRole('ORGANIZADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal Long idUsuario) {
        tipoIngressoService.deletar(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}
