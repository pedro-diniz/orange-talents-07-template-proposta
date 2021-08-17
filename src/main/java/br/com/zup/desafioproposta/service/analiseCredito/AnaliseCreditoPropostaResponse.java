package br.com.zup.desafioproposta.service.analiseCredito;

public class AnaliseCreditoPropostaResponse {

    private String documento;
    private String nome;
    private ResultadoSolicitacao resultadoSolicitacao;
    private String idProposta;

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

    public AnaliseCreditoProposta toEntity() {
        return new AnaliseCreditoProposta(
                documento,
                nome,
                resultadoSolicitacao,
                idProposta
        );
    }
}
