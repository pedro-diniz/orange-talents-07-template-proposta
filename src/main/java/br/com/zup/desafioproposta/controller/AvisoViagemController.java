package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.controller.dto.request.AvisoViagemRequest;
import br.com.zup.desafioproposta.model.AvisoViagem;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.repository.AvisoViagemRepository;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.service.associaCartao.Aviso;
import br.com.zup.desafioproposta.service.avisoLegadoCartao.AvisoLegadoCartaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Problema(400, "Dados inválidos. Verifique o número do seu cartão ou se você " +
                            "está ocultando algum dado da requisição."));
        }

        Optional<Cartao> possivelCartao = cartaoRepository.findById(idCartao);
        if (possivelCartao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Problema(404, "Cartão não encontrado"));
        }

        Cartao cartao = possivelCartao.get();

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
