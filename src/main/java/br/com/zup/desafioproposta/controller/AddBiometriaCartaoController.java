package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.ProblemaHttp;
import br.com.zup.desafioproposta.controller.dto.request.BiometriaRequest;
import br.com.zup.desafioproposta.model.Biometria;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.repository.BiometriaRepository;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class AddBiometriaCartaoController {

    private final BiometriaRepository biometriaRepository;
    private final CartaoRepository cartaoRepository;
    private final Logger logger = LoggerFactory.getLogger(AddBiometriaCartaoController.class);

    public AddBiometriaCartaoController(BiometriaRepository biometriaRepository, CartaoRepository cartaoRepository) {
        this.biometriaRepository = biometriaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @RequestMapping("/cartoes/{idCartao}/biometrias")
    public ResponseEntity<?> adicionarBiometria (UriComponentsBuilder uriBuilder, @PathVariable String idCartao,
                                                 @RequestBody @Valid BiometriaRequest biometriaRequest) {

        if (!Cartao.cartaoValido(idCartao)) {
            logger.warn("Cartão inválido");
            return ProblemaHttp.BAD_REQUEST.getResponse("Dados inválidos. Verifique o número do seu cartão.");
        }

        logger.info("Buscando o cartão");
        Optional<Cartao> possivelCartao = cartaoRepository.findById(idCartao);
        if (possivelCartao.isEmpty()) {
            logger.warn("Cartão não encontrado");
            return ProblemaHttp.NOT_FOUND.getResponse("Cartão não encontrado");
        }
        Cartao cartao = possivelCartao.get();
        logger.info("Cartão encontrado");

        Biometria biometria = biometriaRequest.toModel(cartao);
        logger.info("Biometria criada");
        biometriaRepository.save(biometria);
        logger.info("Biometria salva");

        // Retorno HTTP 201 com o ID da biometria gerada
        return ResponseEntity.created(
                uriBuilder.path("/cartoes/"+idCartao+"/biometrias/{id}").buildAndExpand(biometria.getId()).toUri())
                .build();
    }
}
