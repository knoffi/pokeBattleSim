package com.example.demo;

import java.io.IOException;

import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;
import com.example.demo.Searches.PokemonSearch.StatBySearch;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		try {
			PokemonSearch pikachu = Pokedex.getPokemon("pikachu", RequestMode.JAVA_11);
			StatBySearch hp = pikachu.stats[0];
			System.out.println("name: " + pikachu.name);
			System.out.println(hp.base_stat);
			System.out.println(hp.stat.name);
		} catch (IOException | InterruptedException | RuntimeException e) {
			System.out.println(e);
		}
	}

}
