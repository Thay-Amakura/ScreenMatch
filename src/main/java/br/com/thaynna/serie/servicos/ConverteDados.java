package br.com.thaynna.serie.servicos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class ConverteDados implements IConverteDados {

    // 1. Definindo exceções customizadas
    public static class ConversaoJsonException extends RuntimeException {
        public ConversaoJsonException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class FormatoJsonInvalidoException extends ConversaoJsonException {
        public FormatoJsonInvalidoException(String json, Class<?> classe, Throwable cause) {
            super(String.format("JSON inválido para conversão para %s: %s", 
                classe.getSimpleName(), json), cause);
        }
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            // 2. Validação básica do input
            if (json == null || json.trim().isEmpty()) {
                throw new IllegalArgumentException("JSON não pode ser nulo ou vazio");
            }
            
            return mapper.readValue(json, classe);
            
        } catch (InvalidFormatException e) {
            // 3. Exceção específica para formato inválido
            throw new FormatoJsonInvalidoException(json, classe, e);
        } catch (JsonProcessingException e) {
            // 4. Exceção genérica para outros erros de processamento
            throw new ConversaoJsonException("Falha ao processar JSON", e);
        }
    }
}

