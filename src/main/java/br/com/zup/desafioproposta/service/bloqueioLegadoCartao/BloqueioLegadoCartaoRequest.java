package br.com.zup.desafioproposta.service.bloqueioLegadoCartao;

public class BloqueioLegadoCartaoRequest {

    private String sistemaResponsavel;

    public BloqueioLegadoCartaoRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

}
