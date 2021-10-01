package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PokemonApplication {

	public static void main(String[] args) {

		if (System.getenv("GOOGLE_APPLICATION_CREDENTIALS") == null) {
			System.out.println(
					"Environment variable GOOGLE_APPLICATION_CREDENTIALS is null. It needs to equal the location of your google translation API key. The system can work without it, but you can not call an updated of any translations.");

		}
		if (System.getenv("JSON_BIN_API_KEY") == null) {
			System.out.println(
					"Environment variable JSON_BIN_API_KEY is null. It needs to equal the API key of knoffi's collection on jsonbin.io.\n Alternatively, you can also create your own collection on jsonbin.io and then use your own API key. Your collection needs to contain a copy of knoffi's json-file though.\n \n Feel free to contact knoffi via github to get a copy of this json-file or to help you during the setup. :- )");
			System.exit(-1);

		}
		SpringApplication.run(PokemonApplication.class, args);
	}

}
