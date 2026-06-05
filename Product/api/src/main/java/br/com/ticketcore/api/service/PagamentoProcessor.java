package br.com.ticketcore.api.service;

import br.com.ticketcore.api.dao.EventoDAO;
import br.com.ticketcore.api.dao.IngressoDAO;
import br.com.ticketcore.api.dao.NotificacaoDAO;
import br.com.ticketcore.api.dao.TipoIngressoDAO;
import br.com.ticketcore.api.dao.TransacaoDAO;
import br.com.ticketcore.api.model.dto.CompraRequest;
import br.com.ticketcore.api.model.Evento;
import br.com.ticketcore.api.model.Ingresso;
import br.com.ticketcore.api.model.dto.ItemCompra;
import br.com.ticketcore.api.model.Notificacao;
import br.com.ticketcore.api.model.TipoIngresso;
import br.com.ticketcore.api.model.enums.StatusIngresso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class PagamentoProcessor {

    private static final Logger log = LoggerFactory.getLogger(PagamentoProcessor.class);

    private final TransacaoDAO transacaoDAO;
    private final IngressoDAO ingressoDAO;
    private final NotificacaoDAO notificacaoDAO;
    private final EventoDAO eventoDAO;
    private final TipoIngressoDAO tipoIngressoDAO;

    public PagamentoProcessor(TransacaoDAO transacaoDAO,
                              IngressoDAO ingressoDAO,
                              NotificacaoDAO notificacaoDAO,
                              EventoDAO eventoDAO,
                              TipoIngressoDAO tipoIngressoDAO) {
        this.transacaoDAO = transacaoDAO;
        this.ingressoDAO = ingressoDAO;
        this.notificacaoDAO = notificacaoDAO;
        this.eventoDAO = eventoDAO;
        this.tipoIngressoDAO = tipoIngressoDAO;
    }

    @Async("pagamentoExecutor")
    public void processar(Long idTransacao, Long idComprador, CompraRequest compra) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        boolean aprovado = new Random().nextInt(100) < 80;

        if (aprovado) {
            try {
                transacaoDAO.atualizarStatus(idTransacao, "C");

                ItemCompra primeiroItem = compra.itens().get(0);
                Long idOrganizador = eventoDAO.buscarOrganizadorPorTipoIngresso(primeiroItem.idTipoIngresso());
                TipoIngresso tipoIngresso = tipoIngressoDAO.buscarPorId(primeiroItem.idTipoIngresso());
                Evento evento = eventoDAO.buscarPorId(tipoIngresso.getIdEvento());
                String nmEvento = evento.getNmEvento();

                for (ItemCompra item : compra.itens()) {
                    for (int i = 0; i < item.quantidade(); i++) {
                        Ingresso ingresso = new Ingresso();
                        ingresso.setIdTransacao(idTransacao);
                        ingresso.setIdTipoIngresso(item.idTipoIngresso());
                        ingresso.setCdAcesso(UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
                        ingresso.setStIngresso(StatusIngresso.ATIVO);
                        ingressoDAO.salvar(ingresso);
                    }
                }

                Notificacao notifComprador = new Notificacao();
                notifComprador.setIdUsuario(idComprador);
                notifComprador.setDsMensagem("Compra confirmada! Seus ingressos para o evento \"" + nmEvento + "\" estão disponíveis.");
                notifComprador.setChLida("N");
                notificacaoDAO.salvar(notifComprador);

                if (idOrganizador != null) {
                    Notificacao notifOrganizador = new Notificacao();
                    notifOrganizador.setIdUsuario(idOrganizador);
                    notifOrganizador.setDsMensagem("Nova venda confirmada para o evento \"" + nmEvento + "\".");
                    notifOrganizador.setChLida("N");
                    notificacaoDAO.salvar(notifOrganizador);
                }

            } catch (Exception e) {
                log.error("Erro ao processar pagamento aprovado para transação {}: {}", idTransacao, e.getMessage());
                try {
                    transacaoDAO.atualizarStatus(idTransacao, "X");
                    Notificacao notifErro = new Notificacao();
                    notifErro.setIdUsuario(idComprador);
                    notifErro.setDsMensagem("Houve um erro ao processar sua compra. Tente novamente.");
                    notifErro.setChLida("N");
                    notificacaoDAO.salvar(notifErro);
                } catch (Exception ex) {
                    log.error("Erro ao reverter transação {}: {}", idTransacao, ex.getMessage());
                }
            }

        } else {
            try {
                transacaoDAO.atualizarStatus(idTransacao, "X");

                Notificacao notifComprador = new Notificacao();
                notifComprador.setIdUsuario(idComprador);
                notifComprador.setDsMensagem("Pagamento recusado. Tente novamente ou use outro método de pagamento.");
                notifComprador.setChLida("N");
                notificacaoDAO.salvar(notifComprador);

            } catch (Exception e) {
                log.error("Erro ao registrar rejeição da transação {}: {}", idTransacao, e.getMessage());
            }
        }
    }
}