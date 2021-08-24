package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.google.cloud.translate.*;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {
		Translate translate = TranslateOptions.getDefaultInstance().getService();
		
		Translation translation = translate.translate("¡Hola Mundo!");
		System.out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());
		SpringApplication.run(PokemonApplication.class, args);
	}

}
