package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.NotificacaoResponse;
import br.com.ticketcore.api.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacaoResponse>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(
                notificacaoService.listarPorUsuario(idUsuario).stream()
                        .map(NotificacaoResponse::from)
                        .toList()
        );
    }

    @PatchMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }
}
