package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.IngressoResponse;
import br.com.ticketcore.api.service.IngressoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ingressos", description = "Consulta e cancelamento de ingressos")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('COMPRADOR')")
@RestController
@RequestMapping("/api/ingressos")
public class IngressoController {

    private final IngressoService ingressoService;

    public IngressoController(IngressoService ingressoService) {
        this.ingressoService = ingressoService;
    }

    @Operation(summary = "Buscar por ID", description = "Retorna um ingresso do comprador autenticado pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<IngressoResponse> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(IngressoResponse.from(ingressoService.buscarPorId(id, idUsuario)));
    }

    @Operation(summary = "Buscar por código de acesso", description = "Retorna um ingresso do comprador autenticado pelo código de acesso")
    @GetMapping("/codigo/{codigoAcesso}")
    public ResponseEntity<IngressoResponse> buscarPorCodigoAcesso(
            @PathVariable String codigoAcesso,
            @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(IngressoResponse.from(ingressoService.buscarPorCodigoAcesso(codigoAcesso, idUsuario)));
    }

    @Operation(summary = "Listar por transação", description = "Retorna todos os ingressos do comprador autenticado vinculados a uma transação")
    @GetMapping("/transacao/{idTransacao}")
    public ResponseEntity<List<IngressoResponse>> listarPorTransacao(
            @PathVariable Long idTransacao,
            @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(
                ingressoService.listarPorTransacao(idTransacao, idUsuario).stream()
                        .map(IngressoResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Cancelar ingresso", description = "Cancela um ingresso do comprador autenticado pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal Long idUsuario) {
        ingressoService.cancelar(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}
