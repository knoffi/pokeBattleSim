package com.example.demo;

import java.io.IOException;

import com.example.demo.Combat.Combat;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		try {
			PokemonSearch[] pokemons = Pokedex.getRandomTeam(RequestMode.JAVA_11);
			Combat combat = new Combat(pokemons[0].convert(), pokemons[1].convert());
			var combatResult = combat.getResult();
			for (String text : combatResult.commentary) {
				System.out.println(text);
			}
		} catch (IOException | RuntimeException | InterruptedException e) {
			System.out.println(e);
		}
	}

}
