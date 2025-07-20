package br.com.thaynna.serie;

import br.com.thaynna.serie.Model.DadosSerie;
import br.com.thaynna.serie.Servicos.ConsumoApi;
import br.com.thaynna.serie.Servicos.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SerieApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SerieApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		ConsumoApi consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=the+good+doctor&apikey=f2f470e0");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}

}
