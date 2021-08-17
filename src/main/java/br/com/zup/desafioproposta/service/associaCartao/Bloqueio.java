package br.com.zup.desafioproposta.service.associaCartao;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
public class Bloqueio {

    @Id
    private String id;
    private OffsetDateTime bloqueadoEm;
    private String sistemaResponsavel;
    private Boolean ativo;

    public Bloqueio(OffsetDateTime bloqueadoEm, String sistemaResponsavel, Boolean ativo) {
        this.bloqueadoEm = bloqueadoEm;
        this.sistemaResponsavel = sistemaResponsavel;
        this.ativo = ativo;
    }

    public String getId() {
        return id;
    }

    public OffsetDateTime getBloqueadoEm() {
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
