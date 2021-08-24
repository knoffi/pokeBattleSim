package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {

		if (System.getenv("GOOGLE_APPLICATION_CREDENTIALS") == null) {
			System.out.println(
					"Environment variable GOOGLE_APPLICATION_CREDENTIALS is null. It needs to equal the location of your google translation API key.");
			System.exit(-1);

		}
		;
		SpringApplication.run(PokemonApplication.class, args);
	}

}
