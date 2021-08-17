package br.com.zup.desafioproposta.service.associaCartao;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
public class Carteira {

    @Id
    private String id;
    private String email;
    private OffsetDateTime associadaEm;
    private String emissor;

    public Carteira(String email, OffsetDateTime associadaEm, String emissor) {
        this.email = email;
        this.associadaEm = associadaEm;
        this.emissor = emissor;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public OffsetDateTime getAssociadaEm() {
        return associadaEm;
    }

    public String getEmissor() {
        return emissor;
    }

    public Carteira() {
    }
}
