package br.com.zup.desafioproposta.repository;

import br.com.zup.desafioproposta.model.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
}
