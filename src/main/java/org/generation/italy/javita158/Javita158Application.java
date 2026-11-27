package org.generation.italy.javita158;

import org.generation.italy.javita158.config.CompanyJpaConfiguration;
import org.generation.italy.javita158.config.TropicoJpaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication // L'annotazione fa sì che spring boot cofniguri l'applicazione con tutte le librerie nelle dipendencies
@Import({TropicoJpaConfiguration.class, CompanyJpaConfiguration.class})
public class Javita158Application {

	public static void main(String[] args) {
		SpringApplication.run(Javita158Application.class, args);
	}

}
