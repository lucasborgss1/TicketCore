package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.IngressoResponse;
import br.com.ticketcore.api.service.IngressoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ingressos", description = "Consulta e cancelamento de ingressos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/ingressos")
public class IngressoController {

    private final IngressoService ingressoService;

    public IngressoController(IngressoService ingressoService) {
        this.ingressoService = ingressoService;
    }

    @Operation(summary = "Buscar por ID", description = "Retorna um ingresso pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<IngressoResponse> buscarPorId(@PathVariable Long id) {
        var ingresso = ingressoService.buscarPorId(id);
        if (ingresso == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(IngressoResponse.from(ingresso));
    }

    @Operation(summary = "Buscar por código de acesso", description = "Retorna um ingresso pelo código de acesso gerado na compra")
    @GetMapping("/codigo/{codigoAcesso}")
    public ResponseEntity<IngressoResponse> buscarPorCodigoAcesso(@PathVariable String codigoAcesso) {
        var ingresso = ingressoService.buscarPorCodigoAcesso(codigoAcesso);
        if (ingresso == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(IngressoResponse.from(ingresso));
    }

    @Operation(summary = "Listar por transação", description = "Retorna todos os ingressos vinculados a uma transação de compra")
    @GetMapping("/transacao/{idTransacao}")
    public ResponseEntity<List<IngressoResponse>> listarPorTransacao(@PathVariable Long idTransacao) {
        return ResponseEntity.ok(
                ingressoService.listarPorTransacao(idTransacao).stream()
                        .map(IngressoResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Cancelar ingresso", description = "Cancela um ingresso pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        ingressoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
