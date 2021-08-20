package br.com.zup.desafioproposta.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class RequisicaoBloqueio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant instanteRequisicao = Instant.now();

    private String ipCliente;

    private String userAgent;

    @OneToOne
    private Cartao cartao;

    public RequisicaoBloqueio(String ipCliente, String userAgent, Cartao cartao) {
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    public RequisicaoBloqueio() {
    }
}
