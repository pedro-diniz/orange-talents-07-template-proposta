package br.com.zup.desafioproposta.repository;

import br.com.zup.desafioproposta.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, String> {

}
