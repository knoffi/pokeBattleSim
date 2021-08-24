package com.example.demo.Translater;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.RequestMode;
import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.NameHolder;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class Translater {
    private final static String POKE_MOVE_PATH = "api/v2/move/";
    private final static String POKEMON_SPECIES_PATH = "api/v2/pokemon-species/";

    // TODO: make this into a bean (add constructor which maps officialLanguageKey
    // to data field pokeAPILanguageKey by TranslationKeyMapper)

    public static String getTranslatedName(String englishName, Languages language) {
        try {
            String specificPath = POKEMON_SPECIES_PATH + englishName;
            TranslationHolder translations = Pokedex.getPokeData(specificPath, TranslationHolder.class,
                    RequestMode.JAVA_11);
            String languageKey = TranslationKeyMapper.getKey(language.key);
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

    public static String getTranslatedAttack(String englishAttack, Languages language) {
        try {
            String specificPath = POKE_MOVE_PATH + englishAttack;
            TranslationHolder translations = Pokedex.getPokeData(specificPath, TranslationHolder.class,
                    RequestMode.JAVA_11);
            String languageKey = TranslationKeyMapper.getKey(language.key);
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
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        Translation translation = translate.translate(englishText, Translate.TranslateOption.sourceLanguage("en"),
                Translate.TranslateOption.targetLanguage(languageParam), Translate.TranslateOption.model("base"));
        return translation.getTranslatedText();
    }

    public static List<String> getTranslatedTexts(List<String> englishTexts, String languageParam) {
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        List<Translation> translations = translate.translate(englishTexts,
                Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(languageParam),
                Translate.TranslateOption.model("base"));
        List<String> translatedList = translations.stream().map(translation -> translation.getTranslatedText())
                .collect(Collectors.toList());
        return translatedList;
    }
}

class TranslationHolder {
    public Name[] names;
}

class Name {
    public String name;
    public NameHolder language;
}
