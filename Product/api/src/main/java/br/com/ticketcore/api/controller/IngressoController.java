package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.IngressoResponse;
import br.com.ticketcore.api.service.IngressoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingressos")
public class IngressoController {

    private final IngressoService ingressoService;

    public IngressoController(IngressoService ingressoService) {
        this.ingressoService = ingressoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngressoResponse> buscarPorId(@PathVariable Long id) {
        var ingresso = ingressoService.buscarPorId(id);
        if (ingresso == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(IngressoResponse.from(ingresso));
    }

    @GetMapping("/codigo/{codigoAcesso}")
    public ResponseEntity<IngressoResponse> buscarPorCodigoAcesso(@PathVariable String codigoAcesso) {
        var ingresso = ingressoService.buscarPorCodigoAcesso(codigoAcesso);
        if (ingresso == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(IngressoResponse.from(ingresso));
    }

    @GetMapping("/transacao/{idTransacao}")
    public ResponseEntity<List<IngressoResponse>> listarPorTransacao(@PathVariable Long idTransacao) {
        return ResponseEntity.ok(
                ingressoService.listarPorTransacao(idTransacao).stream()
                        .map(IngressoResponse::from)
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        ingressoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}