package com.example.demo;

import java.io.IOException;

import com.example.demo.SupportedAttacks.AttackStore;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		try {

			AttackStore.update();
		} catch (IOException | RuntimeException e) {
			System.out.println(e);
		}
	}

}
