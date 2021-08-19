package br.com.zup.desafioproposta.controller.dto.request;

import br.com.zup.desafioproposta.config.validation.IsBase64;
import br.com.zup.desafioproposta.model.Biometria;
import br.com.zup.desafioproposta.model.Cartao;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {

    @NotBlank
    @IsBase64
    private String digital;

    public String getDigital() {
        return digital;
    }

    public Biometria toModel(Cartao cartao) {
        return new Biometria(
                digital,
                cartao
        );
    }
}
