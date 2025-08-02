package br.com.thaynna.serie.servicos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {

    public String obterDados(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (IOException | InterruptedException e) {
            // Aqui, se quiser, pode restaurar o estado de interrupção da thread:
            Thread.currentThread().interrupt(); // opção recomendada
            throw new TraducaoException("Erro ao consumir a API", e); // Exceção personalizada
        }
    }
}