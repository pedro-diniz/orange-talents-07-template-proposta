package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.controller.dto.request.PropostaRequest;
import br.com.zup.desafioproposta.service.analiseCredito.AnaliseCreditoProposta;
import br.com.zup.desafioproposta.model.Proposta;
import br.com.zup.desafioproposta.service.analiseCredito.ResultadoSolicitacao;
import br.com.zup.desafioproposta.repository.PropostaRepository;
import br.com.zup.desafioproposta.service.analiseCredito.AnalisaCreditoPropostaService;
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
    public ResponseEntity<?> criar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder) {

        Proposta proposta = request.toModel();

        AnaliseCreditoProposta analiseCredito = analisaCreditoPropostaService.analisaCredito(proposta.toAnaliseDeCredito());

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
            return ResponseEntity.notFound().build();
        }

    }
}
