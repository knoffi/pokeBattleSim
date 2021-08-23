package com.example.demo;

import com.example.demo.Combat.PhraseStore.PhraseStore;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {

		// SpringApplication.run(PokemonApplication.class, args);
		PhraseStore.update();
	}

}
