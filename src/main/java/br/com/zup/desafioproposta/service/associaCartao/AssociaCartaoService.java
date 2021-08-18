package br.com.zup.desafioproposta.service.associaCartao;

import br.com.zup.desafioproposta.config.exception.ServicoIndisponivelException;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.Proposta;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.repository.PropostaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AssociaCartaoService {

    private PropostaRepository propostaRepository;
    private CartaoRepository cartaoRepository;

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${associaCartaoApiUrl.urlCompleta}")
    private String url;

    public AssociaCartaoService(PropostaRepository propostaRepository, CartaoRepository cartaoRepository) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @Async
    @Scheduled(fixedRate = 60000)
    public void printaRecorrentemente() throws InterruptedException {

        List<Proposta> propostas = propostaRepository.findByElegivelSemCartaoAssociado();

        // Verifico quais cartões ainda não foram
        for (Proposta proposta : propostas) {

            // Cria a HTTP request para Associação de Cartão a partir da Proposta não associada
                // e recebe um # de cartão referente àquela proposta
            try {
                RestTemplate restTemplate = new RestTemplate();
                CartaoResponse cartaoResponse = restTemplate.postForObject(
                        url, proposta.toAssociacao(), CartaoResponse.class);

                // Transforma a resposta recebida em um objeto da classe entidade
                Cartao cartao = cartaoResponse.toEntity();

                // Salva o cartão de crédito gerado
                cartaoRepository.save(cartao);

            }
            catch (Exception e) {
                throw new ServicoIndisponivelException("Conexão recusada com o sistema de cartões de crédito");
            }
        }

    }

}
