package br.com.thaynna.serie.servicos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConsultaMyMemory {
    
     // Construtor privado para evitar instanciamento
    private ConsultaMyMemory() {
        throw new UnsupportedOperationException("Classe utilitária - não pode ser instanciada.");
    }
    // Exceção customizada para erros de tradução
    public static class TraducaoException extends RuntimeException {
        public TraducaoException(String message) {
            super(message);
        }
        public TraducaoException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String obterTraducao(String text) throws TraducaoException {
        if (text == null || text.isBlank()) {
            throw new TraducaoException("Texto para tradução não pode ser vazio");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            ConsumoApi consumo = new ConsumoApi();

            String texto = URLEncoder.encode(text, "UTF-8");
            String langpair = URLEncoder.encode("en|pt-br", "UTF-8");

            String url = "https://api.mymemory.translated.net/get?q=" + texto + "&langpair=" + langpair;

            String json = consumo.obterDados(url);

            DadosTraducao traducao = mapper.readValue(json, DadosTraducao.class);
            return traducao.dadosResposta().textoTraduzido();

        } catch (UnsupportedEncodingException e) {
            throw new TraducaoException("Codificação de caracteres não suportada", e);
        } catch (JsonProcessingException e) {
            throw new TraducaoException("Erro ao processar resposta da API", e);
        } catch (RuntimeException e) {
            throw new TraducaoException("Erro na comunicação com o serviço de tradução", e);
        }
    }
}