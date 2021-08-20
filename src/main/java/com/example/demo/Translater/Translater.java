package com.example.demo.Translater;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import com.example.demo.RequestMode;
import com.example.demo.Pokedex.Pokedex;
import com.example.demo.Searches.PokemonSearch.NameHolder;

public class Translater {
    private final static String POKE_MOVE_PATH = "api/v2/move/";
    private final static String POKEMON_SPECIES_PATH = "api/v2/pokemon-species/";

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
            System.out.println("___FAIL ON TRANSLATED NAME___" + e.getClass());
        }
        return englishName;
    }
}

class TranslationHolder {
    public Name[] names;
}

class Name {
    public String name;
    public NameHolder language;
}

class TranslationKeyMapper {
    private static HashMap<String, String> keyMap = new HashMap<String, String>();
    static {
        keyMap.put("en", "en");
        keyMap.put("de", "de");
    }

    public static String getKey(String languageParam) {
        return keyMap.get(languageParam);
    }
}
