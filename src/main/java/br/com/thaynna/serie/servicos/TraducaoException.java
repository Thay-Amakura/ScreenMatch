package br.com.thaynna.serie.servicos;

public class TraducaoException extends RuntimeException {
    public TraducaoException(String message) {
        super(message);
    }
    public TraducaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
