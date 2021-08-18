package br.com.zup.desafioproposta.service.analiseCredito;

import br.com.zup.desafioproposta.config.exception.ServicoIndisponivelException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AnalisaCreditoPropostaService {

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${analisaCreditoApiUrl.urlCompleta}")
    private String analiseCreditoUrl;

    public AnaliseCreditoProposta analisaCredito(AnaliseCreditoRequest request) throws JsonProcessingException {


        // Cria a HTTP request para Análise de Crédito a partir da Proposta não analisada
            // e recebe um # de cartão referente àquela proposta
        try {
            RestTemplate restTemplate = new RestTemplate();
            AnaliseCreditoPropostaResponse analiseResponse = restTemplate.postForObject(
                    analiseCreditoUrl, request, AnaliseCreditoPropostaResponse.class);

            AnaliseCreditoProposta analise = analiseResponse.toEntity();

            return analise;

        }
        catch (HttpClientErrorException e) {

            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                AnaliseCreditoPropostaResponse analiseResponse = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), AnaliseCreditoPropostaResponse.class);
                AnaliseCreditoProposta analiseCreditoProposta = analiseResponse.toEntity();

                return analiseCreditoProposta;
            }
            throw new ServicoIndisponivelException("Conexão recusada com o sistema de análise de crédito");
        }

    }

}
