package br.com.thaynna.serie.Model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio){
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numeroEpisodio();

        try{
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch(NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try {
        this.dataLancamento = LocalDate.parse(dadosEpisodio.dataDeLancamento());
        } catch (DateTimeParseException ex) {
        this.dataLancamento = null;
        }
    }
    public Integer getTemporada() {
        return temporada;
    }
    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }
    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }
    public double getAvaliacao() {
        return avaliacao;
    }
    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }
    public LocalDate getDataLancamento() {
        return dataLancamento;
    }
    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "Temporada=" + temporada + ", titulo=" + titulo + ", numeroEpisodio=" + numeroEpisodio
                + ", avaliacao=" + avaliacao + ", dataLancamento=" + dataLancamento;
    }

    
}
