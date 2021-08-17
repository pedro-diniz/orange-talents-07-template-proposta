package br.com.zup.desafioproposta.service.analiseCredito;

public class AnaliseCreditoProposta {

    private String documento;
    private String nome;
    private ResultadoSolicitacao resultadoSolicitacao;
    private String idProposta;

    @Override
    public String toString() {
        return "AnaliseCreditoProposta{" +
                "documento='" + documento + '\'' +
                ", nome='" + nome + '\'' +
                ", resultadoSolicitacao='" + resultadoSolicitacao + '\'' +
                ", idProposta='" + idProposta + '\'' +
                '}';
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public ResultadoSolicitacao getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public AnaliseCreditoProposta(String documento, String nome, ResultadoSolicitacao resultadoSolicitacao,
                                  String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }
}
