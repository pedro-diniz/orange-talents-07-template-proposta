package br.com.zup.desafioproposta.controller.dto.request;

import br.com.zup.desafioproposta.model.Endereco;

import javax.validation.constraints.NotBlank;

public class EnderecoRequest {

    @NotBlank
    private String endereco;

    private String numero;

    @NotBlank
    private String bairro;

    private String complemento;

    private String cep;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;

    public String getEndereco() {
        return endereco;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getCep() {
        return cep;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public Endereco toModel() {
        return new Endereco(
                endereco,
                numero,
                bairro,
                complemento,
                cep,
                cidade,
                uf
        );
    }
}
