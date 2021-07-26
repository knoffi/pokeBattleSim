package com.example.demo;

import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		List<String> pokemons = Pokedex.getClassicalPokemons();
		String print = pokemons.stream().reduce("", (cur, next) -> cur + "\n" + next);
		System.out.println(print);
	}

}
