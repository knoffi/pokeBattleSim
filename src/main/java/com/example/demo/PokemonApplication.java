package com.example.demo;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {

		// SpringApplication.run(PokemonApplication.class, args);
		try {
			Translate translate = TranslateOptions.newBuilder()
					.setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(
							"C:/Users/monop/programming/pokeFightApi/jsons/ace-case-323614-5db3d27519e9.json")))
					.build().getService();

			Translation translation = translate.translate("¡Hola Mundo!",
					Translate.TranslateOption.sourceLanguage("es"), Translate.TranslateOption.targetLanguage("de"),
					Translate.TranslateOption.model("base"));
			System.out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());
		} catch (IOException e) {
			System.out.println(e.getClass());
		}
		;
	}

}
