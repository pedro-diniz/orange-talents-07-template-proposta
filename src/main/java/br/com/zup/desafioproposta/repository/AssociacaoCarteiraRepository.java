package br.com.zup.desafioproposta.repository;

import br.com.zup.desafioproposta.model.AssociacaoCarteira;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.TipoCarteira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssociacaoCarteiraRepository extends JpaRepository<AssociacaoCarteira, Long> {

    Long countByCartao_IdAndTipoCarteira(String idCartao, TipoCarteira tipoCarteira);

}
