package br.com.zup.desafioproposta.service.associaCartao;

import br.com.zup.desafioproposta.model.Cartao;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private OffsetDateTime associadaEm = OffsetDateTime.now();
    private String emissor;

    @ManyToOne
    private Cartao cartao;

    public Carteira(String email, String emissor, Cartao cartao) {
        this.email = email;
        this.emissor = emissor;
        this.cartao = cartao;
    }

    public Long getId() {
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
