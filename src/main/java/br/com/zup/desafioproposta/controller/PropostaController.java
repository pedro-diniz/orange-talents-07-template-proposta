package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.controller.dto.request.PropostaRequest;
import br.com.zup.desafioproposta.model.Proposta;
import br.com.zup.desafioproposta.repository.PropostaRepository;
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

    public PropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder) {

        Proposta proposta = request.toModel();
        System.out.println(proposta);
        propostaRepository.save(proposta);

        return ResponseEntity.created(
                uriBuilder.path("/propostas/{id}").buildAndExpand(proposta.getId()).toUri())
                .build();

    }
}
