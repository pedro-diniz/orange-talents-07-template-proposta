package br.com.zup.desafioproposta.config.exception;

public class ServidorInternoException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ServidorInternoException(String message) {
        super(message);
    }
}
