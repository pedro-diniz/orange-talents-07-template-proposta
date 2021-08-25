package br.com.zup.desafioproposta.model;

import br.com.zup.desafioproposta.controller.dto.response.PropostaResponse;
import br.com.zup.desafioproposta.service.analiseCredito.AnaliseCreditoRequest;
import br.com.zup.desafioproposta.service.associaCartao.AssociaCartaoRequest;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Base64;

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

    @Enumerated(EnumType.STRING)
    private EstadoProposta estadoProposta;

    public Proposta(String documento, String email, String nome, Endereco endereco, BigDecimal salario) {
        this.documento = encoda(documento);
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
                ", estadoProposta=" + estadoProposta +
                '}';
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Long getId() {
        return id;
    }

    public void tornaClienteElegivel() {
        this.estadoProposta = EstadoProposta.ELEGIVEL;
    }

    public void tornaClienteInelegivel() {
        this.estadoProposta = EstadoProposta.NAO_ELEGIVEL;
    }

    public AnaliseCreditoRequest toAnaliseDeCredito() {
        return new AnaliseCreditoRequest(
                decoda(documento),
                nome,
                String.valueOf(id)
        );
    }

    public AssociaCartaoRequest toAssociacao() {
        return new AssociaCartaoRequest(
                decoda(documento),
                nome,
                String.valueOf(id)
        );
    }

    public Proposta() {
    }

    public PropostaResponse toResponse() {
        return new PropostaResponse(
                decoda(documento),
                email,
                nome,
                endereco,
                salario,
                estadoProposta
        );
    }

    private String encoda(String documento) {
        return Base64.getEncoder().encodeToString(documento.getBytes());
    }

    private String decoda(String documento) {
        return new String(Base64.getDecoder().decode(documento));
    }
}
