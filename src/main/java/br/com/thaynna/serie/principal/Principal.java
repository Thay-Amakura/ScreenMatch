package br.com.thaynna.serie.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.thaynna.serie.Model.DadosSerie;
import br.com.thaynna.serie.Model.DadosTemporada;
import br.com.thaynna.serie.Model.Serie;
import br.com.thaynna.serie.Servicos.ConsumoApi;
import br.com.thaynna.serie.Servicos.ConverteDados;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=f2f470e0";
    private List<DadosSerie> dadosSeries = new ArrayList<>();


        int opcao = -1;
        String nomeSerie;
        String nomeEpisodio;

    public void exibeMenu(){

        while (opcao != 0){
            var menu = """
                    1 - Buscar Série
                    2 - Buscar Episódio
                    3 - Histórico de Busca de Série

                    0 - Sair
                    """;
            
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch(opcao){
                case 1 -> {
                    buscaSerie();
                }
                case 2 -> {
                    System.out.println("Digite o nome do Episódio: ");
                    nomeEpisodio = leitura.nextLine();
                    buscaEpisodioSerie();
                }
                case 3 -> {
                    System.out.println("====== Histórico de Busca de Séries ======");
                    historicoDeBuscaDeSerie();
                }
                case 0 ->{
                    System.out.println("Até a próxima!!");
                }
                default -> {
                    System.out.println("Opção inválida. Tente novamente!");
                }
            }
        }
    }

    private void buscaSerie() {
        DadosSerie dados = getDadosSerie();
        dadosSeries.add(dados);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscaEpisodioSerie(){
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void historicoDeBuscaDeSerie(){
        List<Serie> series = new ArrayList<>();
        series = dadosSeries.stream()
                .map(d -> new Serie(d))
                .collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
    
}
