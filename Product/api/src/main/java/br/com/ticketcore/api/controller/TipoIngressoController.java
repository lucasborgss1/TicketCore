package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.TipoIngresso;
import br.com.ticketcore.api.model.dto.TipoIngressoRequest;
import br.com.ticketcore.api.model.dto.TipoIngressoResponse;
import br.com.ticketcore.api.service.TipoIngressoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-ingresso")
public class TipoIngressoController {

    private final TipoIngressoService tipoIngressoService;

    public TipoIngressoController(TipoIngressoService tipoIngressoService) {
        this.tipoIngressoService = tipoIngressoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoIngressoResponse> buscarPorId(@PathVariable Long id) {
        TipoIngresso tipo = tipoIngressoService.buscarPorId(id);
        if (tipo == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(TipoIngressoResponse.from(tipo));
    }

    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<List<TipoIngressoResponse>> listarPorEvento(@PathVariable Long idEvento) {
        return ResponseEntity.ok(
                tipoIngressoService.listarPorEvento(idEvento).stream()
                        .map(TipoIngressoResponse::from)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody TipoIngressoRequest request) {
        TipoIngresso tipo = new TipoIngresso();
        tipo.setIdEvento(request.idEvento());
        tipo.setNmTipo(request.nmTipo());
        tipo.setVlPreco(request.vlPreco());
        tipo.setQtLote(request.qtLote());
        tipoIngressoService.salvar(tipo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id,
                                          @RequestBody TipoIngressoRequest request) {
        TipoIngresso tipo = new TipoIngresso();
        tipo.setIdEvento(request.idEvento());
        tipo.setNmTipo(request.nmTipo());
        tipo.setVlPreco(request.vlPreco());
        tipo.setQtLote(request.qtLote());
        tipoIngressoService.atualizar(id, tipo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tipoIngressoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
