package br.com.zup.desafioproposta.model;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.OffsetDateTime;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String destino;

    @NotNull
    @Future
    private OffsetDateTime dataTermino;

    private String ipCliente;

    @NotBlank
    private String userAgent;

    private Instant instanteAviso = Instant.now();

    @ManyToOne
    private Cartao cartao;

    public AvisoViagem() {
    }

    public AvisoViagem(String destino, OffsetDateTime dataTermino, String ipCliente, String userAgent, Cartao cartao) {
        this.destino = destino;
        this.dataTermino = dataTermino;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    @Override
    public String toString() {
        return "AvisoViagem{" +
                "id=" + id +
                ", destino='" + destino + '\'' +
                ", dataTermino=" + dataTermino +
                ", ipCliente='" + ipCliente + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", instanteAviso=" + instanteAviso +
                '}';
    }
}
