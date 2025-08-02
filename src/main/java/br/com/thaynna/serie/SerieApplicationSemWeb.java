package br.com.thaynna.serie;

import br.com.thaynna.serie.principal.Principal;
import br.com.thaynna.serie.repositorio.SerieRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SerieApplicationSemWeb implements CommandLineRunner {

	@Autowired
	private SerieRepositorio repositorio;
	public static void main(String[] args) {
		SpringApplication.run(SerieApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();

	}

}
