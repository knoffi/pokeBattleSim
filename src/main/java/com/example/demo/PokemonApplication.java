package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		// try {
		// 	Pokemon[] blueTeam =
		// 	Arrays.stream(Pokedex.getRandomTeam(RequestMode.JAVA_11)).map(search->search.convert()).toArray(Pokemon[]::new);
		// 	Pokemon[] redTeam =
		// 	Arrays.stream(Pokedex.getRandomTeam(RequestMode.JAVA_11)).map(search->search.convert()).toArray(Pokemon[]::new);
		// 	Trainer red = new Trainer("Garry", redTeam);
		// 	Trainer blue = new Trainer("Mace", blueTeam);
		// 	CombatResult result = new TrainerDuell(red, blue).letThemFight();
		// 	for (String text : result.commentary) {
		// 	System.out.println(text);
		// 	}
		// } catch (IOException | RuntimeException | InterruptedException e) {
		// 	System.out.println(e);
		// }
		SpringApplication.run(PokemonApplication.class, args);
	}

}
