package com.example.demo.Translater;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.NameHolder;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class Translater {
    private final static String POKE_MOVE_PATH = "api/v2/move/";
    private final static String POKEMON_SPECIES_PATH = "api/v2/pokemon-species/";

    // TODO: make this into a bean (add constructor which maps officialLanguageKey
    // to data field pokeAPILanguageKey by TranslationKeyMapper)

    public static String getTranslatedName(String englishName, String languageParam) {
        try {
            String specificPath = POKEMON_SPECIES_PATH + englishName;
            TranslationHolder translations = Pokedex.getPokeData(specificPath, TranslationHolder.class,
                    RequestMode.JAVA_11);
            String languageKey = TranslationKeyMapper.getKey(languageParam);
            Optional<Name> translation = Arrays.stream(translations.names)
                    .filter(name -> name.language.name.equals(languageKey)).findFirst();
            if (translation.isPresent()) {
                return translation.get().name;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAIL ON TRANSLATED POKEMON NAME___" + e.getClass());
        }
        return englishName;
    }

    public static String getTranslatedAttack(String englishAttack, String languageParam) {
        try {
            String specificPath = POKE_MOVE_PATH + englishAttack;
            TranslationHolder translations = Pokedex.getPokeData(specificPath, TranslationHolder.class,
                    RequestMode.JAVA_11);
            String languageKey = TranslationKeyMapper.getKey(languageParam);
            Optional<Name> translation = Arrays.stream(translations.names)
                    .filter(name -> name.language.name.equals(languageKey)).findFirst();
            if (translation.isPresent()) {
                return translation.get().name;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("___FAIL ON TRANSLATED ATTACK NAME___" + e.getClass());
        }
        return englishAttack;
    }

    public static String getTranslatedText(String englishText, String languageParam) {
        try {
            Translate translate = TranslateOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(
                            "C:/Users/monop/programming/pokeFightApi/jsons/ace-case-323614-5db3d27519e9.json")))
                    .build().getService();

            Translation translation = translate.translate(englishText, Translate.TranslateOption.sourceLanguage("en"),
                    Translate.TranslateOption.targetLanguage(languageParam), Translate.TranslateOption.model("base"));
            return translation.getTranslatedText();
        } catch (IOException e) {
            System.out.println("___FAIL ON FINDING FILE WITH API KEY___" + e.getClass());
            return englishText;
        }
    }

    public static List<String> getTranslatedTexts(List<String> englishTexts, String languageParam) {
        try {
            Translate translate = TranslateOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(
                            "C:/Users/monop/programming/pokeFightApi/jsons/ace-case-323614-5db3d27519e9.json")))
                    .build().getService();

            List<Translation> translations = translate.translate(englishTexts,
                    Translate.TranslateOption.sourceLanguage("en"),
                    Translate.TranslateOption.targetLanguage(languageParam), Translate.TranslateOption.model("base"));
            List<String> translatedList = translations.stream().map(translation -> translation.getTranslatedText())
                    .collect(Collectors.toList());
            return translatedList;
        } catch (IOException e) {
            System.out.println("___FAIL ON FINDING FILE WITH API KEY___" + e.getClass());
            return englishTexts;
        }
    }
}

class TranslationHolder {
    public Name[] names;
}

class Name {
    public String name;
    public NameHolder language;
}
