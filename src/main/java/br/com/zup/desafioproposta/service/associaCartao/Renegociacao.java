package br.com.zup.desafioproposta.service.associaCartao;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Renegociacao {

    @Id
    private String id;
    private Long quantidade;
    private BigDecimal valor;
    private Instant dataDeCriacao;

    public Renegociacao(Long quantidade, BigDecimal valor, Instant dataDeCriacao) {
        this.quantidade = quantidade;
        this.valor = valor;
        this.dataDeCriacao = dataDeCriacao;
    }

    public String getId() {
        return id;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Instant getDataDeCriacao() {
        return dataDeCriacao;
    }

    public Renegociacao() {
    }
}
