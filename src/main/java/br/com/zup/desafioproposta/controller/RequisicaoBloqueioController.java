package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.EstadoCartao;
import br.com.zup.desafioproposta.model.RequisicaoBloqueio;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.repository.RequisicaoBloqueioRepository;
import br.com.zup.desafioproposta.service.associaCartao.Bloqueio;
import br.com.zup.desafioproposta.service.bloqueioLegadoCartao.BloqueiaLegadoCartaoService;
import br.com.zup.desafioproposta.service.bloqueioLegadoCartao.BloqueioLegadoCartaoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Problema(400, "Dados inválidos." +
                    " Verifique o número do seu cartão ou se você está ocultando algum dado da requisição."));
        }

        Optional<Cartao> possivelCartao = cartaoRepository.findById(idCartao);
        if (possivelCartao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Problema(404, "Cartão não encontrado"));
        }

        Cartao cartao = possivelCartao.get();

        for(Bloqueio bloqueio: cartao.getBloqueios()) {
            if (bloqueio.getAtivo() || cartao.getEstadoCartao() == EstadoCartao.BLOQUEADO) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                        new Problema(422, "Este cartão já está bloqueado"));
            }
        }

        RequisicaoBloqueio requisicaoBloqueio = new RequisicaoBloqueio(
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                cartao
        );

        requisicaoBloqueioRepository.save(requisicaoBloqueio);

        ResponseEntity<?> possivelBloqueio = bloqueiaLegadoCartaoService.bloqueioLegadoCartao(
                new BloqueioLegadoCartaoRequest("desafio-proposta"), cartao);

        if (possivelBloqueio.getStatusCode() != HttpStatus.OK) {
            return possivelBloqueio;
        }

        Bloqueio bloqueio = (Bloqueio) possivelBloqueio.getBody();

        cartao.bloqueia(bloqueio);
        cartaoRepository.save(cartao);

        return ResponseEntity.ok().build();

    }
}
