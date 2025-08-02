package br.com.thaynna.serie.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.thaynna.serie.model.Categoria;
import br.com.thaynna.serie.model.DadosSerie;
import br.com.thaynna.serie.model.DadosTemporada;
import br.com.thaynna.serie.model.Episodio;
import br.com.thaynna.serie.model.Serie;
import br.com.thaynna.serie.repositorio.SerieRepositorio;
import br.com.thaynna.serie.servicos.ConsumoApi;
import br.com.thaynna.serie.servicos.ConverteDados;

public class Principal {
    private static final Logger logger = LoggerFactory.getLogger(Principal.class);
    
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=f2f470e0";
    private SerieRepositorio repositorio;

    private List<Serie> novaSerie = new ArrayList<>();

    private Optional<Serie> serieBusca;

    public Principal(SerieRepositorio repositorio) {
        this.repositorio = repositorio;
        logger.info("Inicializando Principal com repositório");
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Busca por titulo
                    5 - Busca série por Autor
                    6 - Top 5 séeries
                    7 - Buscar série por categoria
                    8 - Filtrar série
                    9 - Buscar episódio por trecho
                    10 - Top 5 episódios
                    11 - Buscar episódios a partir de uma data
                                    
                    0 - Sair
                    """;

            logger.info("\n{}", menu);
            try {
                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao) {
                    case 1:
                        buscarSerieWeb();
                        break;
                    case 2:
                        buscarEpisodioPorSerie();
                        break;
                    case 3:
                        listarSeriesBuscadas();
                        break;
                    case 4:
                        buscaPorTitulo();
                        break;
                    case 5:
                        buscarSeriePorAtor();
                        break;
                    case 6:
                        buscarTop5Series();
                        break;
                    case 7:
                        buscarSeriesPorCategoria();
                        break;
                    case 8:
                        filtrarSeriesPorTemporadaEAvaliacao();
                        break;
                    case 9:
                        buscarEpisodioPorTrecho();
                        break;
                    case 10:
                        topEpisodiosPorSerie();
                        break;
                    case 11:
                        buscarEpisodiosDepoisDeUmaData();
                        break;
                    case 0:
                        logger.info("Saindo da aplicação...");
                        break;
                    default:
                        logger.warn("Opção inválida selecionada: {}", opcao);
                }
            } catch (NoSuchElementException e) {
                logger.error("Entrada inválida fornecida pelo usuário", e);
                leitura.nextLine();
                opcao = -1;
            } catch (Exception e) {
                logger.error("Erro inesperado no menu principal", e);
                opcao = 0;
            }
        }
    }

    private void buscarSerieWeb() {
            DadosSerie dados = getDadosSerie();
            Serie serie = new Serie(dados);
            repositorio.save(serie);
            logger.info("Série salva com sucesso: {}", dados);
    }

    private DadosSerie getDadosSerie() {
        logger.info("Digite nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        logger.debug("Buscando série: {}", nomeSerie);
        
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        logger.info("Digite o nome do Episódio: ");
        var nomeSerie = leitura.nextLine();

        Optional <Serie> serie = novaSerie.stream()
            .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
            .findFirst();

            if(serie.isPresent()){

                var serieEncontrada = serie.get();// facilidade para lão precisar repetir Serie.get()senmpre que precisar

                List<DadosTemporada> temporadas = new ArrayList<>();

                logger.info("Buscando episódios para {} temporadas", serieEncontrada.getTotalTemporadas());
                for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                    var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                    DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                    temporadas.add(dadosTemporada);
                    logger.debug("Temporada {} - {} episódios", i, dadosTemporada.episodios().size());
                }
                temporadas.forEach(t -> logger.info(t.toString()));

                List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .toList();

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
            } else {
                logger.info("Serie não encontrada.");
            }
    }

    private void listarSeriesBuscadas() {
        try {
            List<Serie> series = repositorio.findAll();
            logger.info("Listando {} séries buscadas", series.size());
            series.stream()
                    .sorted(Comparator.comparing(Serie::getGenero))
                    .forEach(s -> logger.info(s.toString()));
        } catch (Exception e) {
            logger.error("Falha ao recuperar séries do repositório", e);
        }
    }

    private void buscaPorTitulo() {
        logger.info("Digite o nome da Serie: ");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            logger.info("Dados da Serie: {}", serieBuscada.get());
        } else {
            logger.info("Serie não encontrada: ");
        }
    }

    private void buscarSeriePorAtor() {
        logger.info("Qual o nome para a busca?");
        var nomeAtor = leitura.nextLine();

        logger.info("Avaliações a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);

        logger.info("Séries em que {} trabalhou: {}", nomeAtor, seriesEncontradas);
        seriesEncontradas.forEach(s ->
                logger.info("{} avaliação: {}", s.getTitulo(), s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s ->
                    logger.info("{} avaliação: {}", s.getTitulo() , s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
            logger.info("Deseja buscar séries de que categoria/gênero? ");
            var nomeGenero = leitura.nextLine();

            Categoria categoria = Categoria.fromPortugues(nomeGenero);
            List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
            logger.info("Séries da categoria {}", nomeGenero);
            seriesPorCategoria.forEach(s ->logger.info(s.toString()));
    }

    private void filtrarSeriesPorTemporadaEAvaliacao(){
        logger.info("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        logger.info("Com avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAValiacao(totalTemporadas, avaliacao);
        logger.info("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                logger.info("{}  - avaliação: {}", s.getTitulo(), s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho(){
        logger.info("Qual o nome do episódio para busca?");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                        logger.info("Série: {} Temporada {} - Episódio {} - {\n",
                                        e.getSerie().getTitulo(), e.getTemporada(),
                                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie(){
        buscaPorTitulo();
        if(serieBusca.isPresent()){
                Serie serie = serieBusca.get();
                List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
                topEpisodios.forEach(e ->
                                logger.info("Série: {} Temporada {} - Episódio {} - {} Avaliação {\n",
                                                e.getSerie().getTitulo(), e.getTemporada(),
                                                e.getNumeroEpisodio(), e.getTitulo() ));
        }
    }
    
    private void buscarEpisodiosDepoisDeUmaData(){
        buscaPorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            logger.info("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();
        
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(e ->logger.info(e.toString()));
        }

    }
}