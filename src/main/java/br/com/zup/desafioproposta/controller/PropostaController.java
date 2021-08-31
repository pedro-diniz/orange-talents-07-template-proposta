package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.controller.dto.request.PropostaRequest;
import br.com.zup.desafioproposta.service.analiseCredito.AnaliseCreditoProposta;
import br.com.zup.desafioproposta.model.Proposta;
import br.com.zup.desafioproposta.service.analiseCredito.ResultadoSolicitacao;
import br.com.zup.desafioproposta.repository.PropostaRepository;
import br.com.zup.desafioproposta.service.analiseCredito.AnalisaCreditoPropostaService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    private PropostaRepository propostaRepository;
    private AnalisaCreditoPropostaService analisaCreditoPropostaService;

    public PropostaController(PropostaRepository propostaRepository,
                              AnalisaCreditoPropostaService analisaCreditoPropostaService) {
        this.propostaRepository = propostaRepository;
        this.analisaCreditoPropostaService = analisaCreditoPropostaService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder)
            throws JsonProcessingException {

        Proposta proposta = request.toModel();

        ResponseEntity<?> possivelAnaliseCredito = analisaCreditoPropostaService.analisaCredito(
                proposta.toAnaliseDeCredito());

        if (possivelAnaliseCredito.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            return possivelAnaliseCredito;
        }

        AnaliseCreditoProposta analiseCredito = (AnaliseCreditoProposta) possivelAnaliseCredito.getBody();

        if (analiseCredito.getResultadoSolicitacao() == ResultadoSolicitacao.SEM_RESTRICAO) {
            proposta.tornaClienteElegivel();
        }
        else {
            proposta.tornaClienteInelegivel();
        }
        propostaRepository.save(proposta);

        return ResponseEntity.created(
                uriBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri())
                .build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable @NotNull Long id) {
        Optional<Proposta> possivelProposta = propostaRepository.findById(id);

        if (possivelProposta.isPresent()) {
            return ResponseEntity.ok(possivelProposta.get().toResponse());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Problema(404, "Proposta não encontrada"));
        }

    }
}
