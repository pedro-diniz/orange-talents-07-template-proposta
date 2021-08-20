package br.com.zup.desafioproposta.service.bloqueioLegadoCartao;

import br.com.zup.desafioproposta.config.exception.EntidadeImprocessavelException;
import br.com.zup.desafioproposta.config.exception.ServicoIndisponivelException;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Bloqueio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class BloqueiaLegadoCartaoService {

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${bloqueiaLegadoCartaoApiUrl.urlCompleta}")
    private String bloqueiaLegadoCartaoUrl;

    public Bloqueio bloqueioLegadoCartao(BloqueioLegadoCartaoRequest request, Cartao cartao) {

        try {

            RestTemplate restTemplate = new RestTemplate();
            BloqueioLegadoCartaoResponse response = restTemplate.postForObject(
                    bloqueiaLegadoCartaoUrl+"/"+cartao.getId()+"/bloqueios",
                    request,
                    BloqueioLegadoCartaoResponse.class
            );

            assert response != null;
            if (Objects.equals(response.getResultado(), "BLOQUEADO")) {
                return new Bloqueio(request.getSistemaResponsavel(), cartao);
            }
            else {
                throw new EntidadeImprocessavelException("Este cartão já está bloqueado");
            }
        }

        catch (HttpClientErrorException e) {
            throw new ServicoIndisponivelException("Conexão recusado com o sistema de bloqueio de cartões");
        }

    }

}
