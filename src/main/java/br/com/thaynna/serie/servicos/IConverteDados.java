package br.com.thaynna.serie.servicos;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
