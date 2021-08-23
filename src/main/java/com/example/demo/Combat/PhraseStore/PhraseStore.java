package com.example.demo.Combat.PhraseStore;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import com.example.demo.Translater.Translater;
import com.example.demo.TypeEffects.Effectiveness;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PhraseStore {
    final static private String PHRASES_FILE_PATH = "./pokeBattleSim/src/main/java/com/example/demo/Combat/PhraseStore/PhraseTable.json";

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

    public static String getEffectPhrase(Effectiveness effect, Languages language) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        try {
            Path jsonPath = Paths.get(PHRASES_FILE_PATH);
            PhraseTable table = mapper.readValue(jsonPath.toFile(), PhraseTable.class);
            return table.getEffectPhrase(language, effect);

        } catch (IOException e) {
            System.out.println("___FINDING PHRASE TABLE FAILED___" + e.getClass());
        }
        return PhraseRow.defaultAttackText;
    }

    public static String getAttackPhrase(Languages language) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        try {
            Path jsonPath = Paths.get(PHRASES_FILE_PATH);
            PhraseTable table = mapper.readValue(jsonPath.toFile(), PhraseTable.class);
            return table.getAttackPhrase(language);

        } catch (IOException e) {
            System.out.println("___FINDING PHRASE TABLE FAILED___" + e.getClass());
        }
        return PhraseRow.defaultAttackText;
    }

}

class PhraseTable {
    private PhraseRow[] rows;

    PhraseTable() {
        this.rows = Arrays.stream(Languages.values()).map(language -> new PhraseRow(language))
                .toArray(PhraseRow[]::new);
    }

    public String getAttackPhrase(Languages language) {
        Optional<String> attackText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getAttackText()).findAny();

        if (attackText.isPresent()) {
            return attackText.get();
        } else {
            return PhraseRow.defaultAttackText;
        }
    }

    public String getEffectPhrase(Languages language, Effectiveness effect) {
        Optional<String> effectText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getEffectText(effect)).findAny();

        if (effectText.isPresent()) {
            return effectText.get();
        } else {
            try {
                throw new Exception("LanguageRowNotFound");
            } catch (Exception e) {
                System.out.println("___NO ROW FOR DESIRED LANGUAGE___" + e.getClass());
            }
            return PhraseRow.defaultEffectText;
        }
    }

}

class PhraseRow {
    final static public String defaultAttackText = "XXX uses YYY.";
    final static public String defaultEffectText = "";

    private String languageKey;
    private String veryEffective;
    private String superEffective;
    private String immunEffective;
    private String barelyEffective;
    private String notVeryEffective;
    private String normalEffective;
    private String attackText;

    PhraseRow(Languages language) {
        this.languageKey = language.key;
        this.veryEffective = Translater.getTranslatedText("It is very effective!", language.key);
        this.superEffective = Translater.getTranslatedText("It is super effective!", language.key);
        this.notVeryEffective = Translater.getTranslatedText("It is not very effective!", language.key);
        this.normalEffective = defaultEffectText;
        this.barelyEffective = Translater.getTranslatedText("It is nearly ineffective!", language.key);
        this.immunEffective = Translater.getTranslatedText("Nothing happens!", language.key);
        this.attackText = Translater.getTranslatedText(defaultAttackText, language.key);
    }

    public boolean belongsToLanguage(Languages language) {
        return this.languageKey == language.key;
    }

    public String getAttackText() {
        return this.attackText;
    }

    public String getEffectText(Effectiveness effect) {
        switch (effect) {
            case IMMUN:
                return this.immunEffective;
            case VERY:
                return this.veryEffective;
            case SUPER:
                return this.superEffective;
            case SUPER_BAD:
                return this.barelyEffective;
            case RESISTANT:
                return this.notVeryEffective;

            default:
                return this.normalEffective;
        }
    }

}
