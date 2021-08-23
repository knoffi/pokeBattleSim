package com.example.demo.Combat.PhraseStore;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import com.example.demo.Translater.Translater;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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

}

class PhraseTable {
    private PhraseRow[] row;

    PhraseTable() {
        this.row = Arrays.stream(Languages.values()).map(language -> new PhraseRow(language)).toArray(PhraseRow[]::new);
    }
}

enum Languages {
    DE("de"), KO("ko"), FR("fr"), IT("it"), JA("ja"), ZH("zh-TW"), ES("es");

    public final String key;

    Languages(String key) {
        this.key = key;
    }
}

class PhraseRow {
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
        this.normalEffective = "";
        this.barelyEffective = Translater.getTranslatedText("It is nearly ineffective!", language.key);
        this.immunEffective = Translater.getTranslatedText("Nothing happens!", language.key);
        this.attackText = Translater.getTranslatedText("XXX uses YYY.", language.key);
    }

}
