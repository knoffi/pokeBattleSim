package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		// try {
		// 	PokemonSearch[] test = Pokedex.getRandomTeam(RequestMode.JAVA_11);
		// 	for (PokemonSearch search : test) {
		// 		System.out.println(search.name);
		// 	}
		// } catch (IOException | InterruptedException e) {
		// 	System.out.println("error");
		// }
		SpringApplication.run(PokemonApplication.class, args);
		// TypeStore.getEffectiveness("normal", "fighting");
		// System.out.println(TypeStore.getEffectiveness("normal", "fighting").value);
		// System.out.println(TypeStore.getEffectiveness("normal", "ghost").value);
		// System.out.println(TypeStore.getEffectiveness("fire", "grass").value);
		// System.out.println(TypeStore.getEffectiveness("fire", "water").value);
		// System.out.println(TypeStore.getEffectiveness("ice", "water").value);
	}

}
