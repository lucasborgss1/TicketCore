package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.EventoDAO;
import br.com.ticketcore.api.dao.NotificacaoDAO;
import br.com.ticketcore.api.dao.PerfilDAO;
import br.com.ticketcore.api.dao.TipoIngressoDAO;
import br.com.ticketcore.api.dao.TransacaoDAO;
import br.com.ticketcore.api.dao.UsuarioPerfilDAO;
import br.com.ticketcore.api.exception.RecursoNaoEncontradoException;
import br.com.ticketcore.api.model.Notificacao;
import br.com.ticketcore.api.model.Perfil;
import br.com.ticketcore.api.model.TipoIngresso;
import br.com.ticketcore.api.model.Transacao;
import br.com.ticketcore.api.model.UsuarioPerfil;
import br.com.ticketcore.api.model.dto.CompraRequest;
import br.com.ticketcore.api.model.enums.StatusTransacao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoDAO transacaoDAO;
    private final TipoIngressoDAO tipoIngressoDAO;
    private final NotificacaoDAO notificacaoDAO;
    private final EventoDAO eventoDAO;
    private final PagamentoProcessor pagamentoProcessor;
    private final PerfilDAO perfilDAO;
    private final UsuarioPerfilDAO usuarioPerfilDAO;

    public TransacaoService(TransacaoDAO transacaoDAO,
                            TipoIngressoDAO tipoIngressoDAO,
                            NotificacaoDAO notificacaoDAO,
                            EventoDAO eventoDAO,
                            PagamentoProcessor pagamentoProcessor,
                            PerfilDAO perfilDAO,
                            UsuarioPerfilDAO usuarioPerfilDAO) {
        this.transacaoDAO = transacaoDAO;
        this.tipoIngressoDAO = tipoIngressoDAO;
        this.notificacaoDAO = notificacaoDAO;
        this.eventoDAO = eventoDAO;
        this.pagamentoProcessor = pagamentoProcessor;
        this.perfilDAO = perfilDAO;
        this.usuarioPerfilDAO = usuarioPerfilDAO;
    }

    public Long processar(CompraRequest compra, Long idUsuario) {
        for (var item : compra.itens()) {
            if (tipoIngressoDAO.buscarPorId(item.idTipoIngresso()) == null) {
                throw new IllegalArgumentException(
                        "Tipo de ingresso não encontrado: " + item.idTipoIngresso());
            }
        }

        BigDecimal vlTotal = compra.itens().stream()
                .map(item -> {
                    TipoIngresso tipo = tipoIngressoDAO.buscarPorId(item.idTipoIngresso());
                    return tipo.getVlPreco().multiply(BigDecimal.valueOf(item.quantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Transacao transacao = new Transacao();
        transacao.setIdComprador(idUsuario);
        transacao.setStTransacao(StatusTransacao.PENDENTE);
        transacao.setVlTotal(vlTotal);

        Long idTransacao = transacaoDAO.salvar(transacao);

        Perfil compradorPerfil = perfilDAO.buscarPorNome("COMPRADOR");
        if (compradorPerfil != null) {
            List<UsuarioPerfil> perfisAtuais = usuarioPerfilDAO.buscarPerfisPorUsuario(idUsuario);
            boolean jaEhComprador = perfisAtuais.stream()
                    .anyMatch(p -> p.getIdPerfil().equals(compradorPerfil.getIdPerfil()));
            if (!jaEhComprador) {
                usuarioPerfilDAO.vincularPerfil(idUsuario, compradorPerfil.getIdPerfil());
            }
        }

        pagamentoProcessor.processar(idTransacao, idUsuario, compra);

        return idTransacao;
    }

    public Transacao buscarPorId(Long id, Long idComprador) {
        Transacao transacao = transacaoDAO.buscarPorIdEComprador(id, idComprador);
        if (transacao == null) throw new RecursoNaoEncontradoException();
        return transacao;
    }

    public List<Transacao> listarPorComprador(Long idComprador) {
        return transacaoDAO.listarPorComprador(idComprador);
    }

    public void cancelarTransacao(Long idTransacao, Long idUsuarioSolicitante) {
        if (transacaoDAO.buscarPorIdEComprador(idTransacao, idUsuarioSolicitante) == null) {
            throw new RecursoNaoEncontradoException();
        }
        transacaoDAO.cancelarTransacao(idTransacao, idUsuarioSolicitante);

        Transacao transacao = transacaoDAO.buscarPorId(idTransacao);

        Notificacao notifComprador = new Notificacao();
        notifComprador.setIdUsuario(transacao.getIdComprador());
        notifComprador.setDsMensagem("Sua transação foi cancelada com sucesso.");
        notifComprador.setChLida("N");
        notificacaoDAO.salvar(notifComprador);

        Long idOrganizador = eventoDAO.buscarOrganizadorPorTransacao(idTransacao);
        if (idOrganizador != null) {
            Notificacao notifOrganizador = new Notificacao();
            notifOrganizador.setIdUsuario(idOrganizador);
            notifOrganizador.setDsMensagem("Uma venda para o seu evento foi cancelada.");
            notifOrganizador.setChLida("N");
            notificacaoDAO.salvar(notifOrganizador);
        }
    }
}
