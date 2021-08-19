package br.com.zup.desafioproposta.config.exception;

public class NaoEncontradaException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public NaoEncontradaException(String message) {
        super(message);
    }
}
