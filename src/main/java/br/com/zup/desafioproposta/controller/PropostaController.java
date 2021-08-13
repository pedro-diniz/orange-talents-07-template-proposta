package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.controller.dto.request.PropostaRequest;
import br.com.zup.desafioproposta.service.analiseCredito.AnaliseCreditoProposta;
import br.com.zup.desafioproposta.model.Proposta;
import br.com.zup.desafioproposta.service.analiseCredito.ResultadoSolicitacao;
import br.com.zup.desafioproposta.repository.PropostaRepository;
import br.com.zup.desafioproposta.service.analiseCredito.AnalisaCreditoPropostaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

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
}
