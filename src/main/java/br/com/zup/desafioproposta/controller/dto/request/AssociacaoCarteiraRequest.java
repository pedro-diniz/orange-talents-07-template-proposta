package br.com.zup.desafioproposta.controller.dto.request;

import br.com.zup.desafioproposta.config.validation.ValueOfEnum;
import br.com.zup.desafioproposta.model.AssociacaoCarteira;
import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.TipoCarteira;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AssociacaoCarteiraRequest {

    @Email @NotBlank
    private String email;

    @NotBlank @ValueOfEnum(enumClass = TipoCarteira.class)
    private String carteira;

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }

    public AssociacaoCarteira toModel(Cartao cartao) {
        return new AssociacaoCarteira(
                email,
                TipoCarteira.valueOf(carteira.trim().toUpperCase()),
                cartao
        );
    }
}
