package br.com.zup.desafioproposta.service.associaCartao;

import br.com.zup.desafioproposta.model.Cartao;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant bloqueadoEm = Instant.now();
    private String sistemaResponsavel;
    private Boolean ativo = true;

    @ManyToOne
    private Cartao cartao;

    public Bloqueio(String sistemaResponsavel, Cartao cartao) {
        this.sistemaResponsavel = sistemaResponsavel;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public Instant getBloqueadoEm() {
        return bloqueadoEm;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public Bloqueio() {
    }
}
