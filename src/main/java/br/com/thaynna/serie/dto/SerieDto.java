package br.com.thaynna.serie.dto;

import br.com.thaynna.serie.model.Categoria;

public record SerieDto(Long id,
                        String titulo,
                        Integer totalTemporadas,
                        Double avaliacao,
                        Categoria genero,
                        String atores,
                        String poster,
                        String sinopse)  {

}
