package br.com.zup.desafioproposta.service.associaCartao;

import br.com.zup.desafioproposta.config.exception.NegocioException;
import br.com.zup.desafioproposta.config.exception.ServicoIndisponivelException;
import br.com.zup.desafioproposta.config.exception.ServidorInternoException;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.Proposta;
import br.com.zup.desafioproposta.repository.CartaoRepository;
import br.com.zup.desafioproposta.repository.PropostaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AssociaCartaoService {

    private final PropostaRepository propostaRepository;
    private final CartaoRepository cartaoRepository;
    private final Logger logger = LoggerFactory.getLogger(AssociaCartaoService.class);

    // variável de ambiente para diferenciar localhost dos endereços dos containers do docker
    @Value("${associaCartaoApiUrl.urlCompleta}")
    private String url;

    public AssociaCartaoService(PropostaRepository propostaRepository, CartaoRepository cartaoRepository) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @Async
    @Scheduled(fixedRate = 60000)
    public void printaRecorrentemente() {

        logger.info("Buscando propostas elegíveis não associadas a cartões");
        List<Proposta> propostas = propostaRepository.findByElegivelSemCartaoAssociado();

        // Verifico quais cartões ainda não foram
        for (Proposta proposta : propostas) {

            logger.info("Nova proposta encontrada");

            // Cria a HTTP request para Associação de Cartão a partir da Proposta não associada
                // e recebe um # de cartão referente àquela proposta
            try {
                RestTemplate restTemplate = new RestTemplate();
                CartaoResponse cartaoResponse = restTemplate.postForObject(
                        url, proposta.toAssociacao(), CartaoResponse.class);

                assert cartaoResponse != null;
                Cartao cartao = cartaoResponse.toEntity();
                logger.info("Cartão gerado com sucesso");

                cartaoRepository.save(cartao);
                logger.info("Cartão salvo com sucesso");

            }
            catch (HttpClientErrorException e) {
                e.printStackTrace();
                logger.error("Erro 4xx na comunicação com sistema legado");
                throw new NegocioException("Erro na comunicação com o sistema legado");
            }

            catch (HttpServerErrorException e) {
                e.printStackTrace();
                logger.error("Erro 5xx no sistema de cartões");
                throw new ServidorInternoException("Erro interno no sistema de cartões");
            }

            catch (ResourceAccessException e) {
                e.printStackTrace();
                logger.error("Conexão recusada com o sistema legado");
                throw new ServicoIndisponivelException("Conexão recusada com o sistema de cartões");
            }

            catch (Exception e) {
                e.printStackTrace();
                logger.error("Exceção desconhecida lançada na comunicação com sistema legado");
                throw new RuntimeException("Algo deu muito ruim!");
            }

        }

    }

}
