package br.com.zup.desafioproposta.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String documento;

    @NotBlank
    private String email;

    @NotBlank
    private String nome;

    @Embedded @NotNull
    private Endereco endereco;

    @NotNull
    private BigDecimal salario;

    public Proposta(String documento, String email, String nome, Endereco endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Proposta{" +
                "id=" + id +
                ", documento='" + documento + '\'' +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", endereco=" + endereco +
                ", salario=" + salario +
                '}';
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Long getId() {
        return id;
    }
}
