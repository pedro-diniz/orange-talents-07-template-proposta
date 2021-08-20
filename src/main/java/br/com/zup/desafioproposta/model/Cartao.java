package br.com.zup.desafioproposta.model;

import br.com.zup.desafioproposta.service.associaCartao.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cartao {

    @Id
    private String id;

    private String emitidoEm;

    private String titular;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cartao_id")
    private Set<Bloqueio> bloqueios = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cartao_id")
    private Set<Aviso> avisos = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cartao_id")
    private Set<Carteira> carteiras = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cartao_id")
    private Set<Parcela> parcelas = new HashSet<>();

    private Long limite;

    @OneToOne(cascade = CascadeType.MERGE)
    private Renegociacao renegociacao;

    @OneToOne(cascade = CascadeType.MERGE)
    private Vencimento vencimento;

    @OneToMany
    @JoinColumn(name = "cartao_id")
    private Set<Biometria> biometrias = new HashSet<>();

    private String idProposta;

    @Enumerated(EnumType.STRING)
    private EstadoCartao estadoCartao;

    public Cartao() {
    }

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

    public EstadoCartao getEstadoCartao() {
        return estadoCartao;
    }

    @Override
    public String toString() {
        return "Cartao{" +
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

    public Cartao(String id, String emitidoEm, String titular, Set<Bloqueio> bloqueios, Set<Aviso> avisos,
                  Set<Carteira> carteiras, Set<Parcela> parcelas, Long limite, Renegociacao renegociacao,
                  Vencimento vencimento, String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.bloqueios = bloqueios;
        this.avisos = avisos;
        this.carteiras = carteiras;
        this.parcelas = parcelas;
        this.limite = limite;
        this.renegociacao = renegociacao;
        this.vencimento = vencimento;
        this.idProposta = idProposta;
    }

    public Cartao(String id) {
        this.id = id;
    }

    public static Boolean cartaoValido(String idCartao) {
        return (idCartao.length() == 19
                && idCartao.charAt(4) == '-'
                && idCartao.charAt(9) == '-'
                && idCartao.charAt(14) == '-'
                && idCartao.replace("-","").chars().allMatch(Character::isDigit));
    }

    public void bloqueia(Bloqueio bloqueio) {
        bloqueios.add(bloqueio);
        this.estadoCartao = EstadoCartao.BLOQUEADO;
    }

}
