package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.EntidadeImprocessavelException;
import br.com.zup.desafioproposta.config.exception.NaoEncontradaException;
import br.com.zup.desafioproposta.config.exception.NegocioException;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.EstadoCartao;
import br.com.zup.desafioproposta.model.RequisicaoBloqueio;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.repository.RequisicaoBloqueioRepository;
import br.com.zup.desafioproposta.service.associaCartao.Bloqueio;
import br.com.zup.desafioproposta.service.bloqueioLegadoCartao.BloqueiaLegadoCartaoService;
import br.com.zup.desafioproposta.service.bloqueioLegadoCartao.BloqueioLegadoCartaoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RequisicaoBloqueioController {

    private CartaoRepository cartaoRepository;
    private RequisicaoBloqueioRepository requisicaoBloqueioRepository;
    private BloqueiaLegadoCartaoService bloqueiaLegadoCartaoService;

    public RequisicaoBloqueioController(CartaoRepository cartaoRepository,
                                        RequisicaoBloqueioRepository requisicaoBloqueioRepository,
                                        BloqueiaLegadoCartaoService bloqueiaLegadoCartaoService) {
        this.cartaoRepository = cartaoRepository;
        this.requisicaoBloqueioRepository = requisicaoBloqueioRepository;
        this.bloqueiaLegadoCartaoService = bloqueiaLegadoCartaoService;
    }

    @GetMapping("/cartoes/{idCartao}/requisicoesBloqueio")
    public ResponseEntity<?> requererBloqueio(@PathVariable String idCartao, HttpServletRequest request) {

        if (request.getHeader("User-Agent") == null || !Cartao.cartaoValido(idCartao)) {
            throw new NegocioException("Dados inválidos. " +
                    "Verifique o número do seu cartão ou se você está ocultando algum dado da requisição.");
        }

        Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(
                () -> new NaoEncontradaException("Cartão não encontrado"));

        for(Bloqueio bloqueio: cartao.getBloqueios()) {
            if (bloqueio.getAtivo() || cartao.getEstadoCartao() == EstadoCartao.BLOQUEADO) {
                throw new EntidadeImprocessavelException("Este cartão já está bloqueado");
            }
        }

        RequisicaoBloqueio requisicaoBloqueio = new RequisicaoBloqueio(
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                cartao
        );

        requisicaoBloqueioRepository.save(requisicaoBloqueio);

        Bloqueio bloqueio = bloqueiaLegadoCartaoService.bloqueioLegadoCartao(
                new BloqueioLegadoCartaoRequest("desafio-proposta"), cartao);
        cartao.bloqueia(bloqueio);
        cartaoRepository.save(cartao);

        return ResponseEntity.ok().build();

    }
}
