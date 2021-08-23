package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.NaoEncontradaException;
import br.com.zup.desafioproposta.config.exception.NegocioException;
import br.com.zup.desafioproposta.controller.dto.request.AvisoViagemRequest;
import br.com.zup.desafioproposta.model.AvisoViagem;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.repository.AvisoViagemRepository;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.service.associaCartao.Aviso;
import br.com.zup.desafioproposta.service.avisoLegadoCartao.AvisoLegadoCartaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AvisoViagemController {

    private CartaoRepository cartaoRepository;
    private AvisoViagemRepository avisoViagemRepository;
    private AvisoLegadoCartaoService avisoLegadoCartaoService;

    public AvisoViagemController(CartaoRepository cartaoRepository,
                                 AvisoViagemRepository avisoViagemRepository,
                                 AvisoLegadoCartaoService avisoLegadoCartaoService) {

        this.cartaoRepository = cartaoRepository;
        this.avisoViagemRepository = avisoViagemRepository;
        this.avisoLegadoCartaoService = avisoLegadoCartaoService;
    }

    @PostMapping("/cartoes/{idCartao}/viagens")
    public ResponseEntity<?> criarAvisoViagem(@PathVariable String idCartao,
                                              @RequestBody @Valid AvisoViagemRequest avisoViagemRequest,
                                              HttpServletRequest request) {

        if (request.getHeader("User-Agent") == null || !Cartao.cartaoValido(idCartao)) {
            throw new NegocioException("Dados inválidos. " +
                    "Verifique o número do seu cartão ou se você está ocultando algum dado da requisição.");
        }

        Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(
                () -> new NaoEncontradaException("Cartão não encontrado"));

        AvisoViagem avisoViagem = avisoViagemRequest.toModel(
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                cartao);

        avisoViagemRepository.save(avisoViagem);

        Aviso aviso = avisoLegadoCartaoService.avisoLegadoCartao(avisoViagemRequest.toLegadoRequest(), cartao);

        cartao.adicionaAviso(aviso);
        cartaoRepository.save(cartao);

        return ResponseEntity.ok().build();
    }

}
