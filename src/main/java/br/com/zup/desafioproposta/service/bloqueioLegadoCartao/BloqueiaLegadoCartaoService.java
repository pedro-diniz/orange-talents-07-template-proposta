package br.com.zup.desafioproposta.service.bloqueioLegadoCartao;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Bloqueio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class BloqueiaLegadoCartaoService {

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${bloqueiaLegadoCartaoApiUrl.urlCompleta}")
    private String bloqueiaLegadoCartaoUrl;

    private final Logger logger = LoggerFactory.getLogger(BloqueiaLegadoCartaoService.class);

    public ResponseEntity<?> bloqueioLegadoCartao(BloqueioLegadoCartaoRequest request, Cartao cartao) {

        logger.info("Requisição de bloqueio recebida no sistema legado");

        try {

            RestTemplate restTemplate = new RestTemplate();
            BloqueioLegadoCartaoResponse response = restTemplate.postForObject(
                    bloqueiaLegadoCartaoUrl+"/"+cartao.getId()+"/bloqueios",
                    request,
                    BloqueioLegadoCartaoResponse.class
            );

            assert response != null;
            if (Objects.equals(response.getResultado(), "BLOQUEADO")) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new Bloqueio(request.getSistemaResponsavel(), cartao));
            }
            else {
                logger.warn("Cartão já está bloqueado");
                return Problema.unprocessableEntity("Este cartão já está bloqueado");
            }
        }

        catch (HttpClientErrorException e) {
            e.printStackTrace();
            logger.error("Erro 4xx na comunicação com sistema legado");
            return Problema.badRequest("Erro na comunicação com o sistema legado");
        }

        catch (HttpServerErrorException e) {
            e.printStackTrace();
            logger.error("Erro 5xx na comunicação com sistema legado");
            return Problema.internalServerError("Erro interno sistema de bloqueio de cartões");
        }

        catch (ResourceAccessException e) {
            e.printStackTrace();
            logger.error("Conexão recusada com o sistema legado");
            return Problema.serviceUnavailable("Conexão recusada com o sistema de bloqueio de cartões");
        }

        catch (Exception e) {
            e.printStackTrace();
            logger.error("Exceção desconhecida lançada na comunicação com sistema legado");
            return Problema.internalServerError("Algo deu muito ruim!");
        }

    }

}
