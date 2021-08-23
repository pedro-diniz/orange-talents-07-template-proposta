package br.com.zup.desafioproposta.controller.dto.request;

import br.com.zup.desafioproposta.model.AvisoViagem;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.avisoLegadoCartao.AvisoLegadoCartaoRequest;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class AvisoViagemRequest {

    @NotBlank
    private String destino;

    @NotNull @Future
    private OffsetDateTime dataTermino;

    public String getDestino() {
        return destino;
    }

    public OffsetDateTime getDataTermino() {
        return dataTermino;
    }

    public AvisoViagem toModel(String ipCliente, String userAgent, Cartao cartao) {
        return new AvisoViagem(
                destino,
                dataTermino,
                ipCliente,
                userAgent,
                cartao
        );
    }

    public AvisoLegadoCartaoRequest toLegadoRequest() {

        return new AvisoLegadoCartaoRequest(
                destino,
                dataTermino.toLocalDate().toString()
        );

    }

}
