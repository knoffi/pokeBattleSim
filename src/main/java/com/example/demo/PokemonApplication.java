package com.example.demo;

import java.io.IOException;

import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Searches.PokemonSearch.PokemonSearch;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		try {
			PokemonSearch pikachu = Pokedex.getPokemon("pikachu", RequestMode.JAVA_11);
			Pokemon pika = new Pokemon(pikachu);
			System.out.println(" \n \n \n ");
			pika.print();
		} catch (IOException | InterruptedException | RuntimeException e) {
			System.out.println(e);
		}
	}

}
