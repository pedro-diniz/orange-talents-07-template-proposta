package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.controller.dto.request.PropostaRequest;
import br.com.zup.desafioproposta.service.analiseCredito.AnaliseCreditoProposta;
import br.com.zup.desafioproposta.model.Proposta;
import br.com.zup.desafioproposta.service.analiseCredito.ResultadoSolicitacao;
import br.com.zup.desafioproposta.repository.PropostaRepository;
import br.com.zup.desafioproposta.service.analiseCredito.AnalisaCreditoPropostaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final PropostaRepository propostaRepository;
    private final AnalisaCreditoPropostaService analisaCreditoPropostaService;
    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    public PropostaController(PropostaRepository propostaRepository,
                              AnalisaCreditoPropostaService analisaCreditoPropostaService) {
        this.propostaRepository = propostaRepository;
        this.analisaCreditoPropostaService = analisaCreditoPropostaService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder)
            throws JsonProcessingException {

        Proposta proposta = request.toModel();

        logger.info("Proposta enviada ao sistema de análise de crédito");
        ResponseEntity<?> possivelAnaliseCredito = analisaCreditoPropostaService.analisaCredito(
                proposta.toAnaliseDeCredito());

        // Verifico se a comunicação correu com o sistema legado correu bem. Aqui, podemos receber três status:
            // 200, 422 ou 503. 200 para propostas SEM_RESTRICAO. 422 para COM_RESTRICAO, 503 se deu algo errado.
            // Se o status não for 200 ou 422, recebi um Problema. Nesse caso, retorno-o.
        if (possivelAnaliseCredito.getStatusCode() != HttpStatus.OK &&
                possivelAnaliseCredito.getStatusCode() != HttpStatus.UNPROCESSABLE_ENTITY) {
            return possivelAnaliseCredito;
        }

        AnaliseCreditoProposta analiseCredito = (AnaliseCreditoProposta) possivelAnaliseCredito.getBody();
        logger.info("Proposta processada com sucesso pelo sistema de análise de crédito");

        assert analiseCredito != null;
        if (analiseCredito.getResultadoSolicitacao() == ResultadoSolicitacao.SEM_RESTRICAO) {
            proposta.tornaClienteElegivel();
            logger.info("Proposta elegível");
        }
        else {
            proposta.tornaClienteInelegivel();
            logger.info("Proposta não elegível");
        }

        propostaRepository.save(proposta);
        logger.info("Proposta salva");

        // Retorno HTTP 201 com o ID da proposta gerada
        return ResponseEntity.created(
                uriBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri())
                .build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable @NotNull Long id) {

        logger.info("Buscando a proposta");
        Optional<Proposta> possivelProposta = propostaRepository.findById(id);

        if (possivelProposta.isPresent()) {
            logger.info("Proposta encontrada");
            return ResponseEntity.ok(possivelProposta.get().toResponse());
        }

        logger.warn("Proposta não encontrada");

        // Retorno HTTP 404 quando a proposta não existir
        return Problema.notFound("Proposta não encontrada");

    }
}
