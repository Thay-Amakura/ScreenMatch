package br.com.thaynna.serie;

import br.com.thaynna.serie.principal.Principal;
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
		Principal principal = new Principal();
		principal.exibeMenu();

	}

}
