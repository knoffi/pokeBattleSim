package com.example.demo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		// SpringApplication.run(PokemonApplication.class, args);
		// https://pokeapi.co/api/v2/pokemon/7/
		try {
			URL squirtelURL = new URL("https://pokeapi.co/api/v2/pokemon/7/");
			HttpURLConnection connection = (HttpURLConnection) squirtelURL.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				String responseString = "";
				Scanner scanner = new Scanner(squirtelURL.openStream());
				while (scanner.hasNext()) {
					responseString += scanner.nextLine();
				}
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Pokemon squirtle = mapper.readValue(responseString, Pokemon.class);
				System.out.println((squirtle.name));
				scanner.close();
			} else {
				throw new RuntimeException("Request failed: Http Error " + responseCode);
			}

		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}

}
