package br.com.zup.desafioproposta.service.associaCartao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate validoAte;
    private String destino;

    public Aviso(LocalDate validoAte, String destino) {
        this.validoAte = validoAte;
        this.destino = destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public Aviso() {
    }
}
