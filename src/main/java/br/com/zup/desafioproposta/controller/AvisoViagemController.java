package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.controller.dto.request.AvisoViagemRequest;
import br.com.zup.desafioproposta.model.AvisoViagem;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.repository.AvisoViagemRepository;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.service.associaCartao.Aviso;
import br.com.zup.desafioproposta.service.avisoLegadoCartao.AvisoLegadoCartaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class AvisoViagemController {

    private final CartaoRepository cartaoRepository;
    private final AvisoViagemRepository avisoViagemRepository;
    private final AvisoLegadoCartaoService avisoLegadoCartaoService;
    private final Logger logger = LoggerFactory.getLogger(AvisoViagemController.class);

    public AvisoViagemController(CartaoRepository cartaoRepository,
                                 AvisoViagemRepository avisoViagemRepository,
                                 AvisoLegadoCartaoService avisoLegadoCartaoService) {

        this.cartaoRepository = cartaoRepository;
        this.avisoViagemRepository = avisoViagemRepository;
        this.avisoLegadoCartaoService = avisoLegadoCartaoService;
    }

    @PostMapping("/cartoes/{idCartao}/viagens")
    public ResponseEntity<?> criarAvisoViagem(@PathVariable String idCartao,
                                              @RequestHeader(value = "User-Agent") String userAgent,
                                              @RequestBody @Valid AvisoViagemRequest avisoViagemRequest,
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

        AvisoViagem avisoViagem = avisoViagemRequest.toModel(request.getRemoteAddr(), userAgent, cartao);
        logger.info("Aviso de viagem gerado");

        logger.info("Aviso de viagem enviado para o sistema legado");
        ResponseEntity<?> possivelAviso = avisoLegadoCartaoService.avisoLegadoCartao(
                avisoViagemRequest.toLegadoRequest(), cartao);

        // Verifico se a comunicação correu com o sistema legado correu bem.
            // Se o status não for OK, o retorno é Problema. Nesse caso, retorno-o.
        if (possivelAviso.getStatusCode() != HttpStatus.OK) {
            return possivelAviso;
        }

        Aviso aviso = (Aviso) possivelAviso.getBody();
        logger.info("Aviso de viagem processado com sucesso pelo sistema legado");

        avisoViagemRepository.save(avisoViagem);
        logger.info("Aviso de viagem salvo com sucesso no sistema local");

        cartao.adicionaAviso(aviso);
        logger.info("Aviso de viagem adicionado com sucesso ao cartão no sistema legado");

        cartaoRepository.save(cartao);
        logger.info("Aviso de viagem salvo com sucesso no sistema legado");

        // Retorno HTTP 200
        return ResponseEntity.ok().build();
    }

}
