package br.com.zup.desafioproposta.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Endereco {

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

    public String getUF() {
        return uf;
    }

    public Endereco() {
    }

    public Endereco(String endereco, String numero, String bairro, String complemento, String cep, String cidade, String uf) {
        this.endereco = endereco;
        this.numero = blankIfNullOrBlank(numero);
        this.bairro = bairro;
        this.complemento = blankIfNullOrBlank(complemento);
        this.cep = blankIfNullOrBlank(cep);
        this.cidade = cidade;
        this.uf = uf;
    }

    private String blankIfNullOrBlank(String atributo) {
        if (atributo == null || atributo.isBlank()) {
            return "";
        }
        return atributo;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "endereco='" + endereco + '\'' +
                ", numero='" + numero + '\'' +
                ", bairro='" + bairro + '\'' +
                ", complemento='" + complemento + '\'' +
                ", cep='" + cep + '\'' +
                ", cidade='" + cidade + '\'' +
                ", uf='" + uf + '\'' +
                '}';
    }
}
