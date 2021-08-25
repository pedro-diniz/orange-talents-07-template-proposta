package br.com.zup.desafioproposta.service.avisoLegadoCartao;

import br.com.zup.desafioproposta.config.exception.Problema;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Aviso;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class AvisoLegadoCartaoService {
    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${avisaLegadoCartaoApiUrl.urlCompleta}")
    private String avisaLegadoCartaoUrl;

    public ResponseEntity<?> avisoLegadoCartao(AvisoLegadoCartaoRequest request, Cartao cartao) {

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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new Problema(400, "Aviso de viagem não pôde ser processado.")
                );

            }

        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new Problema(503, "Conexão recusado com o sistema legado de cartões")
            );

        }

    }

}
