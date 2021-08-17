package br.com.zup.desafioproposta.service.associaCartao;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Parcela {

    @Id
    private String id;
    private Long quantidade;
    private BigDecimal valor;

    public Parcela(Long quantidade, BigDecimal valor) {
        this.quantidade = quantidade;
        this.valor = valor;
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

    public Parcela() {
    }
}
