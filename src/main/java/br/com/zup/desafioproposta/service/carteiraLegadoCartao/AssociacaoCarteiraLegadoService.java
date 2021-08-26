package br.com.zup.desafioproposta.service.carteiraLegadoCartao;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.controller.dto.request.AssociacaoCarteiraRequest;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Carteira;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AssociacaoCarteiraLegadoService {

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${carteiraLegadoCartaoApiUrl.urlCompleta}")
    private String carteiraLegadoCartaoUrl;

    public ResponseEntity<?> associacaoCarteiraLegadoCartao(AssociacaoCarteiraRequest request, Cartao cartao) {

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
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                        new Problema(422, "Este cartão já está associado a esta carteira")
                );
            }
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new Problema(503, "Conexão recusado com o sistema de carteiras digitais")
            );
        }

    }

}
