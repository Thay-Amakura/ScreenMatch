package br.com.thaynna.serie.Servicos;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
