package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.Evento;
import br.com.ticketcore.api.model.dto.EventoRequest;
import br.com.ticketcore.api.model.dto.EventoResponse;
import br.com.ticketcore.api.model.dto.ResumoVendasResponse;
import br.com.ticketcore.api.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Eventos", description = "Gerenciamento de eventos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @Operation(summary = "Listar eventos", description = "Retorna todos os eventos cadastrados")
    @GetMapping
    public ResponseEntity<List<EventoResponse>> listar() {
        return ResponseEntity.ok(
                eventoService.listarTodos().stream()
                        .map(EventoResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Buscar por ID", description = "Retorna um evento pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> buscarPorId(@PathVariable Long id) {
        Evento evento = eventoService.buscarPorId(id);
        if (evento == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(EventoResponse.from(evento));
    }

    @Operation(summary = "Listar por organizador", description = "Retorna todos os eventos criados por um organizador")
    @GetMapping("/organizador/{idOrganizador}")
    public ResponseEntity<List<EventoResponse>> listarPorOrganizador(@PathVariable Long idOrganizador) {
        return ResponseEntity.ok(
                eventoService.listarPorOrganizador(idOrganizador).stream()
                        .map(EventoResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Capacidade restante", description = "Retorna a quantidade de ingressos disponíveis para um evento")
    @GetMapping("/{id}/capacidade-restante")
    public ResponseEntity<Integer> calcularCapacidadeRestante(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.calcularCapacidadeRestante(id));
    }

    @Operation(summary = "Resumo de vendas", description = "Retorna o total de ingressos vendidos e receita por evento")
    @GetMapping("/resumo-vendas")
    public ResponseEntity<List<ResumoVendasResponse>> listarResumoVendas() {
        return ResponseEntity.ok(eventoService.listarResumoVendas());
    }

    @Operation(summary = "Cadastrar evento", description = "Cria um novo evento")
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

    @Operation(summary = "Atualizar evento", description = "Atualiza os dados de um evento existente")
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

    @Operation(summary = "Remover evento", description = "Remove um evento pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
