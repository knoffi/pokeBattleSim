package com.example.demo;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		try {
			// List<String> pokemons = Pokedex.getClassicalPokemons(HTTPMode.JAVA_11);
			// String print = pokemons.stream().reduce("", (cur, next) -> cur + "\n" +
			// next);
			// System.out.println(print);
			System.out.println(Pokedex.getPokemon("mew", HTTPMode.JAVA_11).name);
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
	}

}
