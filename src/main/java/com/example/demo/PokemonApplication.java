package com.example.demo;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Combat.PhraseStore.PhraseStore;
import com.example.demo.TypeEffects.Effectiveness;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {

		// SpringApplication.run(PokemonApplication.class, args);
		System.out.println(PhraseStore.getEffectPhrase(Effectiveness.RESISTANT, Languages.DE));
		System.out.println(PhraseStore.getEffectPhrase(Effectiveness.IMMUN, Languages.DE));
		System.out.println(PhraseStore.getEffectPhrase(Effectiveness.VERY, Languages.DE));
		System.out.println(PhraseStore.getEffectPhrase(Effectiveness.SUPER, Languages.DE));
		System.out.println(PhraseStore.getEffectPhrase(Effectiveness.SUPER_BAD, Languages.DE));
		System.out.println(PhraseStore.getEffectPhrase(Effectiveness.NORMAL, Languages.DE));
		System.out.println(PhraseStore.getAttackPhrase(Languages.DE));
	}

}
