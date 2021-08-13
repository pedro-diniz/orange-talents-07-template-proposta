package br.com.zup.desafioproposta.service.analiseCredito;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnalisaCreditoPropostaService {

    public AnaliseCreditoProposta analisaCredito(AnaliseCreditoRequest request) {

        String analiseCreditoUrl = "http://localhost:9999/api/solicitacao";
        RestTemplate restTemplate = new RestTemplate();
        AnaliseCreditoProposta analiseCreditoProposta = restTemplate.postForObject(
                analiseCreditoUrl, request, AnaliseCreditoProposta.class);

        return analiseCreditoProposta;

    }

}
