package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.RequisicaoBloqueio;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.repository.RequisicaoBloqueioRepository;
import br.com.zup.desafioproposta.service.associaCartao.Bloqueio;
import br.com.zup.desafioproposta.service.bloqueioLegadoCartao.BloqueiaLegadoCartaoService;
import br.com.zup.desafioproposta.service.bloqueioLegadoCartao.BloqueioLegadoCartaoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class RequisicaoBloqueioController {

    private final CartaoRepository cartaoRepository;
    private final RequisicaoBloqueioRepository requisicaoBloqueioRepository;
    private final BloqueiaLegadoCartaoService bloqueiaLegadoCartaoService;
    private final Logger logger = LoggerFactory.getLogger(RequisicaoBloqueioController.class);

    public RequisicaoBloqueioController(CartaoRepository cartaoRepository,
                                        RequisicaoBloqueioRepository requisicaoBloqueioRepository,
                                        BloqueiaLegadoCartaoService bloqueiaLegadoCartaoService) {
        this.cartaoRepository = cartaoRepository;
        this.requisicaoBloqueioRepository = requisicaoBloqueioRepository;
        this.bloqueiaLegadoCartaoService = bloqueiaLegadoCartaoService;
    }

    @GetMapping("/cartoes/{idCartao}/requisicoesBloqueio")
    public ResponseEntity<?> requererBloqueio(@PathVariable String idCartao,
                                              @RequestHeader(value = "User-Agent") String userAgent,
                                              HttpServletRequest request) {

        if (!Cartao.cartaoValido(idCartao)) {
            logger.warn("Cartão inválido");
            return Problema.badRequest("Dados inválidos. Verifique o número do seu cartão.");
        }

        logger.info("Buscando o cartão");
        Optional<Cartao> possivelCartao = cartaoRepository.findById(idCartao);
        if (possivelCartao.isEmpty()) {
            logger.warn("Cartão não encontrado");
            return Problema.notFound("Cartão não encontrado");
        }
        Cartao cartao = possivelCartao.get();
        logger.info("Cartão encontrado");

        if (cartao.estaBloqueado()) {
            logger.warn("Cartão já está bloqueado");
            return Problema.unprocessableEntity("Este cartão já está bloqueado");
        }


        RequisicaoBloqueio requisicaoBloqueio = new RequisicaoBloqueio(request.getRemoteAddr(), userAgent, cartao);
        logger.info("Requisição de bloqueio gerada");

        logger.info("Requisição de bloqueio enviada para o sistema legado");
        ResponseEntity<?> possivelBloqueio = bloqueiaLegadoCartaoService.bloqueioLegadoCartao(
                new BloqueioLegadoCartaoRequest("desafio-proposta"), cartao);

        // Verifico se a comunicação correu com o sistema legado correu bem.
            // Se o status não for OK, o retorno é Problema. Nesse caso, retorno-o.
        if (possivelBloqueio.getStatusCode() != HttpStatus.OK) {
            return possivelBloqueio;
        }

        Bloqueio bloqueio = (Bloqueio) possivelBloqueio.getBody();
        logger.info("Requisição de bloqueio processada com sucesso pelo sistema legado");

        requisicaoBloqueioRepository.save(requisicaoBloqueio);
        logger.info("Requisição de bloqueio salva com sucesso no sistema local");

        cartao.bloqueia(bloqueio);
        logger.info("Cartão bloqueado com sucesso no sistema local e legado");

        cartaoRepository.save(cartao);
        logger.info("Bloqueio salvo com sucesso no sistema legado");

        // Retorno HTTP 200
        return ResponseEntity.ok().build();

    }
}
