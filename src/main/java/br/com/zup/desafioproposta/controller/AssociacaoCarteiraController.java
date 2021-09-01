package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.controller.dto.request.AssociacaoCarteiraRequest;
import br.com.zup.desafioproposta.model.AssociacaoCarteira;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.TipoCarteira;
import br.com.zup.desafioproposta.repository.AssociacaoCarteiraRepository;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.service.associaCartao.Carteira;
import br.com.zup.desafioproposta.service.carteiraLegadoCartao.AssociacaoCarteiraLegadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AssociacaoCarteiraController {

    private final CartaoRepository cartaoRepository;
    private final AssociacaoCarteiraRepository associacaoCarteiraRepository;
    private final AssociacaoCarteiraLegadoService associacaoCarteiraLegadoService;
    private final Logger logger = LoggerFactory.getLogger(AssociacaoCarteiraController.class);

    public AssociacaoCarteiraController(CartaoRepository cartaoRepository,
                                        AssociacaoCarteiraRepository associacaoCarteiraRepository,
                                        AssociacaoCarteiraLegadoService associacaoCarteiraLegadoService) {
        this.cartaoRepository = cartaoRepository;
        this.associacaoCarteiraRepository = associacaoCarteiraRepository;
        this.associacaoCarteiraLegadoService = associacaoCarteiraLegadoService;
    }

    @PostMapping("/cartoes/{idCartao}/carteiras")
    public ResponseEntity<?> associarCarteira(@PathVariable String idCartao,
                                              @RequestBody @Valid AssociacaoCarteiraRequest associacaoCarteiraRequest,
                                              UriComponentsBuilder uriComponentsBuilder) {

        if (!Cartao.cartaoValido(idCartao)) {
            logger.warn("Cartão inválido");
            return Problema.badRequest("Verifique o número do seu cartão");
        }

        logger.info("Buscando o cartão");
        Optional<Cartao> possivelCartao = cartaoRepository.findById(idCartao);
        if (possivelCartao.isEmpty()) {
            logger.warn("Cartão não encontrado");
            return Problema.notFound("Cartão não encontrado");
        }
        Cartao cartao = possivelCartao.get();
        logger.info("Cartão encontrado");

        logger.info("Buscando vínculos");
        Long vinculos = associacaoCarteiraRepository.countByCartao_IdAndTipoCarteira(
                idCartao,
                TipoCarteira.valueOf(associacaoCarteiraRequest.getCarteira())
        );

        if (vinculos > 0) {
            logger.warn("Cartão já vinculado a uma carteira deste tipo");
            return Problema.unprocessableEntity("Este cartão já está vinculado a esta carteira");
        }
        logger.info("Nenhum vínculo encontrado");

        AssociacaoCarteira associacaoCarteira = associacaoCarteiraRequest.toModel(cartao);
        logger.info("Associação de carteira gerada");

        logger.info("Associação enviada para o sistema legado");
        ResponseEntity<?> possivelCarteira = associacaoCarteiraLegadoService.associacaoCarteiraLegadoCartao(
                associacaoCarteiraRequest, cartao);

        // Verifico se a comunicação correu com o sistema legado correu bem.
            // Se o status não for OK, o retorno é Problema. Nesse caso, retorno-o.
        if (possivelCarteira.getStatusCode() != HttpStatus.OK) {
            return possivelCarteira;
        }

        Carteira carteira = (Carteira) possivelCarteira.getBody();
        logger.info("Associação processada com sucesso pelo sistema legado");

        associacaoCarteiraRepository.save(associacaoCarteira);
        logger.info("Associação de carteira salva com sucesso no sistema local");

        cartao.vinculaCarteira(carteira);
        logger.info("Associação vinculada com sucesso ao cartão no sistema legado");

        cartaoRepository.save(cartao);
        logger.info("Associação de carteira salva com sucesso no sistema legado");

        // Workaround para conseguir enviar a URI da carteira
        List<Carteira> aux = new ArrayList<>(cartao.getCarteiras());
        Long idCarteira = aux.get(aux.size()-1).getId();

        // Resposta com URI com recurso criado
        return ResponseEntity.created(
                uriComponentsBuilder.path("/cartoes/{id}/carteiras").buildAndExpand(idCarteira).toUri())
                .build();
    }

}
