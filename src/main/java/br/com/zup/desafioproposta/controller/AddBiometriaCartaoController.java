package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.NaoEncontradaException;
import br.com.zup.desafioproposta.controller.dto.request.BiometriaRequest;
import br.com.zup.desafioproposta.model.Biometria;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.repository.BiometriaRepository;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
public class AddBiometriaCartaoController {

    private BiometriaRepository biometriaRepository;
    private CartaoRepository cartaoRepository;

    public AddBiometriaCartaoController(BiometriaRepository biometriaRepository, CartaoRepository cartaoRepository) {
        this.biometriaRepository = biometriaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @RequestMapping("/cartoes/{idCartao}/biometrias")
    public ResponseEntity<?> adicionarBiometria (UriComponentsBuilder uriBuilder, @PathVariable String idCartao,
                                                 @RequestBody @Valid BiometriaRequest biometriaRequest) {
        Cartao cartao = cartaoRepository.findById(idCartao).
                orElseThrow(() -> new NaoEncontradaException("Cartão não encontrado"));

        Biometria biometria = biometriaRequest.toModel(cartao);
        biometriaRepository.save(biometria);

        return ResponseEntity.created(
                uriBuilder.path("/cartoes/"+idCartao+"/biometrias/{id}").buildAndExpand(biometria.getId()).toUri())
                .build();
    }
}
