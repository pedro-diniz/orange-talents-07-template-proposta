package br.com.zup.desafioproposta.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vencimento {

    @Id
    private String id;

    private Long dia;

    private String dataDeCriacao;

    public Vencimento() {
    }

    public Vencimento(String id, Long dia, String dataDeCriacao) {
        this.id = id;
        this.dia = dia;
        this.dataDeCriacao = dataDeCriacao;
    }

    @Override
    public String toString() {
        return "Vencimento{" +
                "id='" + id + '\'' +
                ", dia=" + dia +
                ", dataDeCriacao=" + dataDeCriacao +
                '}';
    }

    public String getId() {
        return id;
    }

    public Long getDia() {
        return dia;
    }

    public String getDataDeCriacao() {
        return dataDeCriacao;
    }
}
