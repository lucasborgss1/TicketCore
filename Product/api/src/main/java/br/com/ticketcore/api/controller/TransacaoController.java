package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.CompraIniciadaResponse;
import br.com.ticketcore.api.model.dto.CompraRequest;
import br.com.ticketcore.api.model.dto.TransacaoResponse;
import br.com.ticketcore.api.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Realizar compra", description = "Inicia o processamento de uma compra de ingressos. Retorna o ID da transação criada.")
    @PostMapping
    public ResponseEntity<CompraIniciadaResponse> processar(@RequestBody CompraRequest compra) {
        Long idTransacao = transacaoService.processar(compra);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new CompraIniciadaResponse(idTransacao,
                        "Pagamento em processamento. Você será notificado em breve."));
    }

    @Operation(summary = "Buscar por ID", description = "Retorna os dados de uma transação pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponse> buscarPorId(@PathVariable Long id) {
        var transacao = transacaoService.buscarPorId(id);
        if (transacao == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(TransacaoResponse.from(transacao));
    }

    @Operation(summary = "Listar por comprador", description = "Retorna todas as transações de um comprador")
    @GetMapping("/comprador/{idComprador}")
    public ResponseEntity<List<TransacaoResponse>> listarPorComprador(@PathVariable Long idComprador) {
        return ResponseEntity.ok(
                transacaoService.listarPorComprador(idComprador).stream()
                        .map(TransacaoResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Cancelar transação", description = "Cancela uma transação e todos os ingressos vinculados a ela")
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id,
                                         @RequestParam Long idUsuarioSolicitante) {
        transacaoService.cancelarTransacao(id, idUsuarioSolicitante);
        return ResponseEntity.ok().build();
    }
}
