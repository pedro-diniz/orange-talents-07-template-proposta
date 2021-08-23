package br.com.zup.desafioproposta.service.carteiraLegadoCartao;

import br.com.zup.desafioproposta.config.exception.EntidadeImprocessavelException;
import br.com.zup.desafioproposta.config.exception.ServicoIndisponivelException;
import br.com.zup.desafioproposta.controller.dto.request.AssociacaoCarteiraRequest;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.associaCartao.Carteira;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AssociacaoCarteiraLegadoService {

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${carteiraLegadoCartaoApiUrl.urlCompleta}")
    private String carteiraLegadoCartaoUrl;

    public Carteira associacaoCarteiraLegadoCartao(AssociacaoCarteiraRequest request, Cartao cartao) {

        try {

            RestTemplate restTemplate = new RestTemplate();
            AssociacaoCarteiraLegadoResponse response = restTemplate.postForObject(
                    carteiraLegadoCartaoUrl+"/"+cartao.getId()+"/carteiras",
                    request,
                    AssociacaoCarteiraLegadoResponse.class
            );

            assert response != null;
            if (Objects.equals(response.getResultado(), "ASSOCIADA")) {
                return new Carteira(request.getEmail(), request.getCarteira(), cartao);
            }
            else {
                throw new EntidadeImprocessavelException("Este cartão já está associado a esta carteira");
            }
        }

        catch (HttpServerErrorException e) {
            throw new ServicoIndisponivelException("Conexão recusado com o sistema de carteiras digitais");
        }

    }

}
