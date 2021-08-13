package com.example.demo;

import com.example.demo.TypeEffects.TypeStore;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		//SpringApplication.run(PokemonApplication.class, args);
		TypeStore.updateTypes();
	}

}
