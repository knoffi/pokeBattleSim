package com.example.demo;

import com.example.demo.TypeEffects.TypeStore;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		// SpringApplication.run(PokemonApplication.class, args);
		// TypeStore.getEffectiveness("normal", "fighting");
		System.out.println(TypeStore.getEffectiveness("normal", "fighting").value);
		System.out.println(TypeStore.getEffectiveness("normal", "ghost").value);
		System.out.println(TypeStore.getEffectiveness("fire", "grass").value);
		System.out.println(TypeStore.getEffectiveness("fire", "water").value);
		System.out.println(TypeStore.getEffectiveness("ice", "water").value);
	}

}
