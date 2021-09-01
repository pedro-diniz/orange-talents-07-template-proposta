package br.com.zup.desafioproposta.service.avisoLegadoCartao;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Aviso;
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

import java.time.LocalDate;
import java.util.Objects;

@Service
public class AvisoLegadoCartaoService {
    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${avisaLegadoCartaoApiUrl.urlCompleta}")
    private String avisaLegadoCartaoUrl;

    private final Logger logger = LoggerFactory.getLogger(AvisoLegadoCartaoService.class);

    public ResponseEntity<?> avisoLegadoCartao(AvisoLegadoCartaoRequest request, Cartao cartao) {

        logger.info("Aviso de viagem recebido no sistema legado");

        try {

            RestTemplate restTemplate = new RestTemplate();
            AvisoLegadoCartaoResponse response = restTemplate.postForObject(
                    avisaLegadoCartaoUrl+"/"+cartao.getId()+"/avisos",
                    request,
                    AvisoLegadoCartaoResponse.class
            );

            assert response != null;
            if (Objects.equals(response.getResultado(), "CRIADO")) {
                return ResponseEntity.status(HttpStatus.OK).body(new Aviso(
                        LocalDate.parse(request.getValidoAte()),
                        request.getDestino()
                ));
            }
            else {
                logger.warn("Aviso de viagem não pôde ser processado");
                return Problema.unprocessableEntity("Aviso de viagem não pôde ser processado.");
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
            return Problema.internalServerError("Erro interno no sistema de aviso de viagens");
        }

        catch (ResourceAccessException e) {
            e.printStackTrace();
            logger.error("Conexão recusada com o sistema legado");
            return Problema.serviceUnavailable("Conexão recusada com o sistema de aviso de viagens");
        }

        catch (Exception e) {
            e.printStackTrace();
            logger.error("Exceção desconhecida lançada na comunicação com sistema legado");
            return Problema.internalServerError("Algo deu muito ruim!");
        }

    }

}
