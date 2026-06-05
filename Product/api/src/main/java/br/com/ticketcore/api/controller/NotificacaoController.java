package br.com.ticketcore.api.controller;

import br.com.ticketcore.api.model.dto.NotificacaoResponse;
import br.com.ticketcore.api.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notificações", description = "Consulta e marcação de notificações do usuário")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @Operation(summary = "Listar notificações do usuário", description = "Retorna todas as notificações de um usuário")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacaoResponse>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(
                notificacaoService.listarPorUsuario(idUsuario).stream()
                        .map(NotificacaoResponse::from)
                        .toList()
        );
    }

    @Operation(summary = "Marcar como lida", description = "Marca uma notificação como lida pelo ID")
    @PatchMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }
}
