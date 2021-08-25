package br.com.zup.desafioproposta.config.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problema {

    private Integer status;
    private OffsetDateTime dataHora = OffsetDateTime.now();
    private String titulo;
    private ValidationErrorOutputDto campos;

    public Integer getStatus() {
        return status;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public String getTitulo() {
        return titulo;
    }

    public ValidationErrorOutputDto getCampos() {
        return campos;
    }

    public Problema(Integer status, String titulo, ValidationErrorOutputDto campos) {
        this.status = status;
        this.titulo = titulo;
        this.campos = campos;
    }

    public Problema(Integer status, String titulo) {
        this.status = status;
        this.titulo = titulo;
    }

}
