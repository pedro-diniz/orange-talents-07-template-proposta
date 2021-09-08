package br.com.zup.desafioproposta.service.carteiraLegadoCartao;

import br.com.zup.desafioproposta.config.exception.ProblemaHttp;
import br.com.zup.desafioproposta.controller.dto.request.AssociacaoCarteiraRequest;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Carteira;
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
public class AssociacaoCarteiraLegadoService {

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${carteiraLegadoCartaoApiUrl.urlCompleta}")
    private String carteiraLegadoCartaoUrl;

    private final Logger logger = LoggerFactory.getLogger(AssociacaoCarteiraLegadoService.class);

    public ResponseEntity<?> associacaoCarteiraLegadoCartao(AssociacaoCarteiraRequest request, Cartao cartao) {

        logger.info("Associação recebida no sistema legado");

        try {

            RestTemplate restTemplate = new RestTemplate();
            AssociacaoCarteiraLegadoResponse response = restTemplate.postForObject(
                    carteiraLegadoCartaoUrl+"/"+cartao.getId()+"/carteiras",
                    request,
                    AssociacaoCarteiraLegadoResponse.class
            );

            assert response != null;
            if (Objects.equals(response.getResultado(), "ASSOCIADA")) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new Carteira(request.getEmail(), request.getCarteira(), cartao));
            }
            else {
                logger.warn("Cartão já vinculado a uma carteira deste tipo");
                return ProblemaHttp.UNPROCESSABLE_ENTITY.getResponse("Este cartão já está associado a esta carteira");
            }
        }

        catch (HttpClientErrorException e) {
            e.printStackTrace();
            logger.error("Erro 4xx na comunicação com sistema legado");
            return ProblemaHttp.BAD_REQUEST.getResponse("Erro na comunicação com o sistema legado");
        }

        catch (HttpServerErrorException e) {
            e.printStackTrace();
            logger.error("Erro 5xx na comunicação com sistema legado");
            return ProblemaHttp.INTERNAL_SERVER_ERROR.getResponse("Erro interno no sistema de associação de carteiras");
        }

        catch (ResourceAccessException e) {
            e.printStackTrace();
            logger.error("Conexão recusada com o sistema legado");
            return ProblemaHttp.SERVICE_UNAVAIABLE.getResponse("Conexão recusada com o sistema de associação de carteiras");
        }

        catch (Exception e) {
            e.printStackTrace();
            logger.error("Exceção desconhecida lançada na comunicação com sistema legado");
            return ProblemaHttp.INTERNAL_SERVER_ERROR.getResponse("Algo deu muito ruim!");
        }

    }

}
