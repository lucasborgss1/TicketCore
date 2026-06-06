package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.CompraIniciadaResponse;
import br.com.ticketcore.api.model.dto.CompraRequest;
import br.com.ticketcore.api.model.dto.TransacaoResponse;
import br.com.ticketcore.api.service.TransacaoService;
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

@Tag(name = "Transações", description = "Compra e cancelamento de ingressos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @Operation(summary = "Realizar compra", description = "Inicia o processamento de uma compra. O ID do comprador é extraído do token JWT.")
    @PostMapping
    public ResponseEntity<CompraIniciadaResponse> processar(
            @Valid @RequestBody CompraRequest compra,
            @AuthenticationPrincipal Long idUsuario) {
        Long idTransacao = transacaoService.processar(compra, idUsuario);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CompraIniciadaResponse(idTransacao,
                        "Pagamento em processamento. Você será notificado em breve."));
    }

    @Operation(summary = "Buscar por ID", description = "Retorna os dados de uma transação do comprador autenticado")
    @PreAuthorize("hasRole('COMPRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponse> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(TransacaoResponse.from(transacaoService.buscarPorId(id, idUsuario)));
    }

    @Operation(summary = "Listar minhas transações", description = "Retorna todas as transações do comprador autenticado")
    @PreAuthorize("hasRole('COMPRADOR')")
    @GetMapping("/minhas")
    public ResponseEntity<List<TransacaoResponse>> listarMinhas(@AuthenticationPrincipal Long idUsuario) {
        return ResponseEntity.ok(
                transacaoService.listarPorComprador(idUsuario).stream()
                        .map(TransacaoResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Cancelar transação", description = "Cancela uma transação e todos os ingressos vinculados. O solicitante é identificado pelo token JWT.")
    @PreAuthorize("hasRole('COMPRADOR')")
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal Long idUsuario) {
        transacaoService.cancelarTransacao(id, idUsuario);
        return ResponseEntity.ok().build();
    }
}
