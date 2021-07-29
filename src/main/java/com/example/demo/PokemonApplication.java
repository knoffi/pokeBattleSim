package com.example.demo;

import java.io.IOException;

import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		try {
			PokemonSearch[] pokemons = Pokedex.getRandomTeam(RequestMode.JAVA_11);
			for(PokemonSearch pokemon:pokemons){
				pokemon.convert().print();
			}
		} catch (IOException | RuntimeException | InterruptedException e) {
			System.out.println(e);
		}
	}

}
