package br.com.zup.desafioproposta.service.analiseCredito;

import br.com.zup.desafioproposta.config.exception.ServicoIndisponivelException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnalisaCreditoPropostaService {

    public AnaliseCreditoProposta analisaCredito(AnaliseCreditoRequest request) {

        String analiseCreditoUrl = "http://localhost:9999/api/solicitacao";

        // Cria a HTTP request para Análise de Crédito a partir da Proposta não analisada
            // e recebe um # de cartão referente àquela proposta
        try {
            RestTemplate restTemplate = new RestTemplate();
            AnaliseCreditoPropostaResponse analiseResponse = restTemplate.postForObject(
                    analiseCreditoUrl, request, AnaliseCreditoPropostaResponse.class);

            AnaliseCreditoProposta analise = analiseResponse.toEntity();

            return analise;

        }
        catch (Exception e) {
            throw new ServicoIndisponivelException("Conexão recusada com o sistema de análise de crédito");
        }

    }

}
