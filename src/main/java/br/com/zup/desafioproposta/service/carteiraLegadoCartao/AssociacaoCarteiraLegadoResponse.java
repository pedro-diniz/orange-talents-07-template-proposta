package br.com.zup.desafioproposta.service.carteiraLegadoCartao;

public class AssociacaoCarteiraLegadoResponse {

    private String resultado;
    private String id;

    public AssociacaoCarteiraLegadoResponse(String resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public AssociacaoCarteiraLegadoResponse() {
    }

    public String getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}
