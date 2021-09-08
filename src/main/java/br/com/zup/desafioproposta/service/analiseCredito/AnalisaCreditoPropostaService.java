package br.com.zup.desafioproposta.service.analiseCredito;

import br.com.zup.desafioproposta.config.exception.ProblemaHttp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

@Service
public class AnalisaCreditoPropostaService {

    private final Logger logger = LoggerFactory.getLogger(AnalisaCreditoPropostaService.class);

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${analisaCreditoApiUrl.urlCompleta}")
    private String analiseCreditoUrl;

    public ResponseEntity<?> analisaCredito(AnaliseCreditoRequest request) throws JsonProcessingException {

        logger.info("Proposta recebida no sistema de análise de crédito");
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

        catch (HttpStatusCodeException e) {

            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {

                AnaliseCreditoPropostaResponse analiseResponse = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), AnaliseCreditoPropostaResponse.class);
                AnaliseCreditoProposta analiseCreditoProposta = analiseResponse.toEntity();

                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(analiseCreditoProposta);

            }
            else if (e instanceof HttpClientErrorException) {
                e.printStackTrace();
                logger.error("Erro 4xx na comunicação com sistema legado");
                return ProblemaHttp.BAD_REQUEST.getResponse("Erro na comunicação com o sistema legado");
            }
            else if (e instanceof HttpServerErrorException) {
                e.printStackTrace();
                logger.error("Erro 5xx na comunicação com sistema legado");
                return ProblemaHttp.INTERNAL_SERVER_ERROR.getResponse("Erro interno no sistema de análise de crédito");
            }
            else {
                e.printStackTrace();
                logger.error("Exceção HTTP desconhecida lançada na comunicação com sistema legado");
                return ProblemaHttp.INTERNAL_SERVER_ERROR.getResponse("Exceção desconhecida");
            }
        }

        catch (ResourceAccessException e) {
            e.printStackTrace();
            logger.error("Conexão recusada com o sistema legado");
            return ProblemaHttp.SERVICE_UNAVAIABLE.getResponse("Conexão recusada com o sistema de análise de crédito");
        }

        catch (Exception e) {
            e.printStackTrace();
            logger.error("Exceção desconhecida lançada na comunicação com sistema legado");
            return ProblemaHttp.INTERNAL_SERVER_ERROR.getResponse("Algo deu muito ruim!");
        }

    }

}
