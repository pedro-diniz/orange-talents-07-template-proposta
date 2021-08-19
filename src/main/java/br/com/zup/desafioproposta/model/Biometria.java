package br.com.zup.desafioproposta.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Base64;

@Entity
public class Biometria {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String digital;

    private Instant criadaEm = Instant.now();

    @NotNull
    @ManyToOne
    private Cartao cartao;

    public Biometria(String digital, Cartao cartao) {
        this.digital = digital;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }
}
