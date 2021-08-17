package br.com.zup.desafioproposta.controller.dto.response;

import br.com.zup.desafioproposta.model.Endereco;
import br.com.zup.desafioproposta.model.EstadoProposta;

import java.math.BigDecimal;

public class PropostaResponse {

    private String documento;
    private String email;
    private String nome;
    private Endereco endereco;
    private BigDecimal salario;
    private EstadoProposta estadoProposta;

    public PropostaResponse() {
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public EstadoProposta getEstadoProposta() {
        return estadoProposta;
    }

    public PropostaResponse(String documento, String email, String nome, Endereco endereco, BigDecimal salario,
                            EstadoProposta estadoProposta) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
        this.estadoProposta = estadoProposta;
    }
}
