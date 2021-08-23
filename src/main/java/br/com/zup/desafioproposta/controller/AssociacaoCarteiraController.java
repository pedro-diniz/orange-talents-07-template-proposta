package br.com.zup.desafioproposta.controller;

import br.com.zup.desafioproposta.config.exception.NaoEncontradaException;
import br.com.zup.desafioproposta.config.exception.NegocioException;
import br.com.zup.desafioproposta.controller.dto.request.AssociacaoCarteiraRequest;
import br.com.zup.desafioproposta.model.AssociacaoCarteira;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.TipoCarteira;
import br.com.zup.desafioproposta.repository.AssociacaoCarteiraRepository;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.service.associaCartao.Carteira;
import br.com.zup.desafioproposta.service.carteiraLegadoCartao.AssociacaoCarteiraLegadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AssociacaoCarteiraController {

    private CartaoRepository cartaoRepository;
    private AssociacaoCarteiraRepository associacaoCarteiraRepository;
    private AssociacaoCarteiraLegadoService associacaoCarteiraLegadoService;

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
                                              HttpServletRequest request,
                                              UriComponentsBuilder uriComponentsBuilder) {

        // Verifica se o ID do cartão passado na URL é válido
        if (!Cartao.cartaoValido(idCartao)) {
            throw new NegocioException("Verifique o número do seu cartão.");
        }

        // Procura o cartão pelo ID e retorna uma exceção caso não o encontre
        Cartao cartao = cartaoRepository.findById(idCartao).orElseThrow(
                () -> new NaoEncontradaException("Cartão não encontrado"));

        // Vê quantos vínculos esse cartão já possui nessa carteira
        Long vinculos = associacaoCarteiraRepository.countByCartao_IdAndTipoCarteira(
                idCartao,
                TipoCarteira.valueOf(associacaoCarteiraRequest.getCarteira())
        );

        // Retorna 422 se já houver um vínculo deste cartão a esta carteira
        if (vinculos > 0) {
            return ResponseEntity.unprocessableEntity().build();
        }

        // Gera uma associação de carteira a partir da requisição
        AssociacaoCarteira associacaoCarteira = associacaoCarteiraRequest.toModel(cartao);

        // Faz a associação no sistema legado primeiro
        Carteira carteira = associacaoCarteiraLegadoService.associacaoCarteiraLegadoCartao(
                associacaoCarteiraRequest, cartao);
        System.out.println(carteira.toString());

        // Se estiver tudo ok, salva a associação no sistema local
        associacaoCarteiraRepository.save(associacaoCarteira);

        // Vinculo o cartão à carteira do sistema legado
        cartao.vinculaCarteira(carteira);

        // Salva a carteira do sistema legado
        cartaoRepository.save(cartao);

        // Workaround para conseguir enviar a URI da carteira
        List<Carteira> aux = new ArrayList<>(cartao.getCarteiras());
        Long idCarteira = aux.get(aux.size()-1).getId();

        // Resposta com URI com recurso criado
        return ResponseEntity.created(
                uriComponentsBuilder.path("/cartoes/{id}/carteiras").buildAndExpand(idCarteira).toUri())
                .build();
    }

}
