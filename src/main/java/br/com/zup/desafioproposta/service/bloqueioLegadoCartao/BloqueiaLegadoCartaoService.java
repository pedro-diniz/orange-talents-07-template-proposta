package br.com.zup.desafioproposta.service.bloqueioLegadoCartao;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Bloqueio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class BloqueiaLegadoCartaoService {

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${bloqueiaLegadoCartaoApiUrl.urlCompleta}")
    private String bloqueiaLegadoCartaoUrl;

    public ResponseEntity<?> bloqueioLegadoCartao(BloqueioLegadoCartaoRequest request, Cartao cartao) {

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
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                        new Problema(422, "Este cartão já está bloqueado")
                );
            }
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new Problema(503, "Conexão recusado com o sistema de bloqueio de cartões")
            );
        }

    }

}
