package br.com.zup.desafioproposta.service.avisoLegadoCartao;

import br.com.zup.desafioproposta.config.exception.NegocioException;
import br.com.zup.desafioproposta.config.exception.ServicoIndisponivelException;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Aviso;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class AvisoLegadoCartaoService {
    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${avisaLegadoCartaoApiUrl.urlCompleta}")
    private String avisaLegadoCartaoUrl;

    public Aviso avisoLegadoCartao(AvisoLegadoCartaoRequest request, Cartao cartao) {

        try {

            RestTemplate restTemplate = new RestTemplate();
            AvisoLegadoCartaoResponse response = restTemplate.postForObject(
                    avisaLegadoCartaoUrl+"/"+cartao.getId()+"/avisos",
                    request,
                    AvisoLegadoCartaoResponse.class
            );

            assert response != null;
            if (Objects.equals(response.getResultado(), "CRIADO")) {
                return new Aviso(
                        LocalDate.parse(request.getValidoAte()),
                        request.getDestino()
                );
            }
            else {
                throw new NegocioException("Aviso de viagem não pôde ser processado.");
            }

        }

        catch (HttpServerErrorException e) {
            throw new ServicoIndisponivelException("Conexão recusado com o sistema legado de cartões");
        }

    }

}
