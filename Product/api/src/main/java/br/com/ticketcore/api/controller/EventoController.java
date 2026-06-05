package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.Evento;
import br.com.ticketcore.api.model.dto.EventoRequest;
import br.com.ticketcore.api.model.dto.EventoResponse;
import br.com.ticketcore.api.model.dto.ResumoVendasResponse;
import br.com.ticketcore.api.service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public ResponseEntity<List<EventoResponse>> listar() {
        return ResponseEntity.ok(
                eventoService.listarTodos().stream()
                        .map(EventoResponse::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> buscarPorId(@PathVariable Long id) {
        Evento evento = eventoService.buscarPorId(id);
        if (evento == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(EventoResponse.from(evento));
    }

    @GetMapping("/organizador/{idOrganizador}")
    public ResponseEntity<List<EventoResponse>> listarPorOrganizador(@PathVariable Long idOrganizador) {
        return ResponseEntity.ok(
                eventoService.listarPorOrganizador(idOrganizador).stream()
                        .map(EventoResponse::from)
                        .toList()
        );
    }

    @GetMapping("/{id}/capacidade-restante")
    public ResponseEntity<Integer> calcularCapacidadeRestante(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.calcularCapacidadeRestante(id));
    }

    @GetMapping("/resumo-vendas")
    public ResponseEntity<List<ResumoVendasResponse>> listarResumoVendas() {
        return ResponseEntity.ok(eventoService.listarResumoVendas());
    }

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody EventoRequest request) {
        Evento evento = new Evento();
        evento.setIdOrganizador(request.idOrganizador());
        evento.setIdCategoria(request.idCategoria());
        evento.setNmEvento(request.nmEvento());
        evento.setDtEvento(request.dtEvento());
        evento.setNmLocal(request.nmLocal());
        evento.setQtCapacidade(request.qtCapacidade());
        evento.setDsImagem(request.dsImagem());
        eventoService.salvar(evento);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id,
                                          @RequestBody EventoRequest request) {
        Evento evento = new Evento();
        evento.setIdOrganizador(request.idOrganizador());
        evento.setIdCategoria(request.idCategoria());
        evento.setNmEvento(request.nmEvento());
        evento.setDtEvento(request.dtEvento());
        evento.setNmLocal(request.nmLocal());
        evento.setQtCapacidade(request.qtCapacidade());
        evento.setDsImagem(request.dsImagem());
        eventoService.atualizar(id, evento);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
