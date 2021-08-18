package br.com.zup.desafioproposta.repository;

import br.com.zup.desafioproposta.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    @Query(value = "SELECT * FROM proposta\n" +
            "\tWHERE proposta.estado_proposta = 'ELEGIVEL' \n" +
            "\tAND proposta.id NOT IN \n" +
            "\t\t(SELECT CAST(cartao.id_proposta AS INTEGER) FROM cartao)", nativeQuery = true)
//    @Query(value= "SELECT * FROM proposta p WHERE p.estado_proposta = 'ELEGIVEL' " +
//            "AND p.id NOT IN (SELECT id_proposta FROM cartao)", nativeQuery = true)
    List<Proposta> findByElegivelSemCartaoAssociado();

}
