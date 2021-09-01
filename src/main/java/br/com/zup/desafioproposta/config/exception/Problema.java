package br.com.zup.desafioproposta.config.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<Problema> notFound(String mensagem) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new Problema(404, mensagem));
    }

    public static ResponseEntity<Problema> unprocessableEntity(String mensagem) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new Problema(422, mensagem));
    }

    public static ResponseEntity<Problema> serviceUnavailable(String mensagem) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                new Problema(503, mensagem));
    }

    public static ResponseEntity<Problema> badRequest(String mensagem) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new Problema(400, mensagem));
    }

    public static ResponseEntity<Problema> internalServerError(String mensagem) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new Problema(500, mensagem));
    }

}
