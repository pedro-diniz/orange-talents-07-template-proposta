package br.com.zup.desafioproposta.controller.dto.request;

import br.com.zup.desafioproposta.config.validation.CpfOuCnpj;
import br.com.zup.desafioproposta.config.validation.UniqueDocument;
import br.com.zup.desafioproposta.config.validation.UniqueValue;
import br.com.zup.desafioproposta.model.Proposta;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

public class PropostaRequest {

    @NotBlank @CpfOuCnpj @UniqueDocument
    private String documento;

    @NotBlank @Email
    private String email;

    @NotBlank
    private String nome;

    @NotNull @Valid
    private EnderecoRequest endereco;

    @NotNull @Positive
    private BigDecimal salario;

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public EnderecoRequest getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public Proposta toModel() {
        return new Proposta(
                documento,
                email,
                nome,
                endereco.toModel(),
                salario
        );
    }
}
