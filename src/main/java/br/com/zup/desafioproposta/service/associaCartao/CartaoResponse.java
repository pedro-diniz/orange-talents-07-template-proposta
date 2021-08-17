package br.com.zup.desafioproposta.service.associaCartao;

import br.com.zup.desafioproposta.model.Cartao;
import br.com.zup.desafioproposta.model.Vencimento;

import java.util.HashSet;
import java.util.Set;

public class CartaoResponse {

    private String id;

    private String emitidoEm;

    private String titular;

    private Set<Bloqueio> bloqueios = new HashSet<>();
    private Set<Aviso> avisos = new HashSet<>();
    private Set<Carteira> carteiras = new HashSet<>();
    private Set<Parcela> parcelas = new HashSet<>();

    private Long limite;

    private Renegociacao renegociacao;

    private Vencimento vencimento;

    private String idProposta;

    public String getId() {
        return id;
    }

    public String getEmitidoEm() {
        return emitidoEm;
    }

    public String getTitular() {
        return titular;
    }

    public Set<Bloqueio> getBloqueios() {
        return bloqueios;
    }

    public Set<Aviso> getAvisos() {
        return avisos;
    }

    public Set<Carteira> getCarteiras() {
        return carteiras;
    }

    public Set<Parcela> getParcelas() {
        return parcelas;
    }

    public Long getLimite() {
        return limite;
    }

    public Renegociacao getRenegociacao() {
        return renegociacao;
    }

    public Vencimento getVencimento() {
        return vencimento;
    }

    public String getIdProposta() {
        return idProposta;
    }

    @Override
    public String toString() {
        return "CartaoResponse{" +
                "id='" + id + '\'' +
                ", emitidoEm='" + emitidoEm + '\'' +
                ", titular='" + titular + '\'' +
                ", bloqueios=" + bloqueios +
                ", avisos=" + avisos +
                ", carteiras=" + carteiras +
                ", parcelas=" + parcelas +
                ", limite=" + limite +
                ", renegociacao=" + renegociacao +
                ", vencimento=" + vencimento +
                ", idProposta='" + idProposta + '\'' +
                '}';
    }

    public CartaoResponse() {
    }

    public Cartao toEntity() {
        return new Cartao(
                id,
                emitidoEm,
                titular,
                bloqueios,
                avisos,
                carteiras,
                parcelas,
                limite,
                renegociacao,
                vencimento,
                idProposta
        );
    }

}
