package br.com.thaynna.serie.controller;

import org.springframework.web.bind.annotation.RestController;
import br.com.thaynna.serie.repositorio.SerieRepositorio;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import br.com.thaynna.serie.dto.SerieDto;



@RestController
public class SerieController {

    @Autowired
    private SerieRepositorio repositorio;

    @GetMapping("/series")
    public List<SerieDto>  obterSeries(){
        return repositorio.findAll()
        .stream()
        .map(s -> new SerieDto(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
        .collect(Collectors.toList());
    }
    
}
