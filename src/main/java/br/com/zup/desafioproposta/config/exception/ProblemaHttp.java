package br.com.zup.desafioproposta.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Retorna uma ResponseEntity que encapsula um objeto do tipo Problema.
 * O objetivo desta classe é ser utilizada para controle de fluxo de requisições HTTP,
 * mais especificamente para evitar o lançamento de exceções. Uma vez que o fluxo natural
 * do sistema é retornar objetos ResponseEntity que contenham algum corpo, essa classe
 * atual nos desvios de fluxo, sendo sempre utilizada para manter o fluxo natural de retornar
 * uma ResponseEntity e, assim, evitar as quebras de fluxo inerentes ao lançamento de exceções.
 */
public enum ProblemaHttp {

    NOT_FOUND {
        @Override
        public ResponseEntity<Problema> getResponse(String msg) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Problema(404, msg));
        }
    },
    BAD_REQUEST {
        @Override
        public ResponseEntity<Problema> getResponse(String msg) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Problema(400, msg));
        }
    },
    SERVICE_UNAVAIABLE {
        @Override
        public ResponseEntity<Problema> getResponse(String msg) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new Problema(503, msg));
        }
    },
    UNPROCESSABLE_ENTITY {
        @Override
        public ResponseEntity<Problema> getResponse(String msg) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    new Problema(422, msg));
        }
    },
    INTERNAL_SERVER_ERROR {
        @Override
        public ResponseEntity<Problema> getResponse(String msg) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new Problema(500, msg));
        }
    };

    /**
     *
     * @param msg mensagem com erro que será retornado ao cliente da API
     * @return ResponseEntity com Status HTTP encapsulando um Problema
     */
    public abstract ResponseEntity<Problema> getResponse(String msg);

}
