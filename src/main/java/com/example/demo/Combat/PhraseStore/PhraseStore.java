package com.example.demo.Combat.PhraseStore;

import java.io.FileWriter;
import java.io.IOException;

import com.example.demo.StoreButler.StoreButler;
import com.example.demo.StoreButler.StoreButlerServices;
import com.example.demo.TypeEffects.Effectiveness;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PhraseStore {
    final static private String PHRASES_FILE_PATH = "http://localhost:8080/PhraseTable.json";
    static private PhraseTable PHRASES = getPhrases();

    public static void update() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        try {
            FileWriter file = new FileWriter(PHRASES_FILE_PATH);
            PhraseTable table = new PhraseTable();
            String data = mapper.writeValueAsString(table);
            file.write(data);
            file.close();

        } catch (IOException e) {
            System.out.println("___WRITING TYPE TABLE FAILED___" + e.getClass());
        }
    }

    private static PhraseTable getPhrases() {
        try {
            PhraseTable table = StoreButler.getData(StoreButlerServices.PHRASES, PhraseTable.class);
            return table;
        } catch (Exception e) {
            System.out.print("___GET REQUEST FOR PHRASE TABLE FAILED___" + e.getClass());
        }
        ;
        return new PhraseTable();
    }

    public static String getEffectPhrase(Effectiveness effect, Languages language) {
        return PHRASES.getEffectPhrase(language, effect);
    }

    public static String getAttackPhrase(Languages language) {
        return PHRASES.getAttackPhrase(language);
    }

    public static String getResultPhrase(Languages language) {
        return PHRASES.getResultPhrase(language);
    }

}
