package br.com.zup.desafioproposta.controller.dto.request;

import br.com.zup.desafioproposta.config.validation.IsFutureOffsetDateTime;
import br.com.zup.desafioproposta.model.AvisoViagem;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.service.avisoLegadoCartao.AvisoLegadoCartaoRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class AvisoViagemRequest {

    @NotBlank
    private String destino;

    @NotNull @IsFutureOffsetDateTime
    private String dataTermino;

    public String getDestino() {
        return destino;
    }

    public OffsetDateTime getDataTermino() {
        return OffsetDateTime.parse(dataTermino);
    }

    public AvisoViagem toModel(String ipCliente, String userAgent, Cartao cartao) {
        return new AvisoViagem(
                destino,
                OffsetDateTime.parse(dataTermino),
                ipCliente,
                userAgent,
                cartao
        );
    }

    public AvisoLegadoCartaoRequest toLegadoRequest() {

        return new AvisoLegadoCartaoRequest(
                destino,
                OffsetDateTime.parse(dataTermino).toLocalDate().toString()
        );

    }

}
