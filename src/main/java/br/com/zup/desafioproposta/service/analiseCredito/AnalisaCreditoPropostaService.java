package br.com.zup.desafioproposta.service.analiseCredito;

import br.com.zup.desafioproposta.config.exception.Problema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class AnalisaCreditoPropostaService {

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${analisaCreditoApiUrl.urlCompleta}")
    private String analiseCreditoUrl;

    public ResponseEntity<?> analisaCredito(AnaliseCreditoRequest request) throws JsonProcessingException {


        // Cria a HTTP request para Análise de Crédito a partir da Proposta não analisada
            // e recebe um # de cartão referente àquela proposta
        try {
            RestTemplate restTemplate = new RestTemplate();
            AnaliseCreditoPropostaResponse analiseResponse = restTemplate.postForObject(
                    analiseCreditoUrl, request, AnaliseCreditoPropostaResponse.class);

            assert analiseResponse != null;
            AnaliseCreditoProposta analise = analiseResponse.toEntity();

            return ResponseEntity.status(HttpStatus.OK).body(analise);

        }
        catch (Exception ex) {

            if (ex instanceof HttpStatusCodeException) {

                HttpStatusCodeException e = (HttpStatusCodeException) ex;
                AnaliseCreditoPropostaResponse analiseResponse = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), AnaliseCreditoPropostaResponse.class);
                AnaliseCreditoProposta analiseCreditoProposta = analiseResponse.toEntity();

                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(analiseCreditoProposta);
            }

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new Problema(503, "Conexão recusada com o sistema de análise de crédito")
            );

        }

    }

}
