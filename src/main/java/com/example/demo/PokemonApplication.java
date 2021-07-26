package com.example.demo;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		try {
			var team = Pokedex.getRandomTeam(HTTPMode.JAVA_11);
			String[] teamNames = Arrays.stream(team).map(pokemon -> pokemon.name).toArray(String[]::new);
			for (String name : teamNames) {
				System.out.println(name + " \n");
			}
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
	}

}
