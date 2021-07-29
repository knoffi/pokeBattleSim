package com.example.demo;

import java.io.IOException;
import java.util.Arrays;

import com.example.demo.Combat.CombatResult;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.Trainer.Trainer;
import com.example.demo.TrainerDuell.TrainerDuell;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		try {
			Pokemon[] blueTeam =
			Arrays.stream(Pokedex.getRandomTeam(RequestMode.JAVA_11)).map(search->search.convert()).toArray(Pokemon[]::new);
			Pokemon[] redTeam =
			Arrays.stream(Pokedex.getRandomTeam(RequestMode.JAVA_11)).map(search->search.convert()).toArray(Pokemon[]::new);
			Trainer red = new Trainer("Garry", redTeam);
			Trainer blue = new Trainer("Mace", blueTeam);
			CombatResult result = new TrainerDuell(red, blue).letThemFight();
			for (String text : result.commentary) {
			System.out.println(text);
			}
		} catch (IOException | RuntimeException | InterruptedException e) {
			System.out.println(e);
		}
	}

}
