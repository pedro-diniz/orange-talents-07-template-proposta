package br.com.zup.desafioproposta.service.avisoLegadoCartao;

public class AvisoLegadoCartaoRequest {

    private String destino;

    private String validoAte;

    public AvisoLegadoCartaoRequest(String destino, String validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public String getValidoAte() {
        return validoAte;
    }
}
