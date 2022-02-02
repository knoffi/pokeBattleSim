package com.example.demo.Combat.PhraseStore;

import java.util.Arrays;
import java.util.Optional;

public enum Languages {
    DE("de"), KO("ko"), FR("fr"), IT("it"), JA("ja"), ZH("zh-TW"), ES("es"), EN("en");

    public final String key;

    Languages(String key) {
        this.key = key;
    }

    public boolean isEuropean() {
        return this.key.equals(DE.key) || this.key.equals(ES.key) || this.key.equals(EN.key) || this.key.equals(FR.key)
                || this.key.equals(IT.key);
    }

    public static Optional<Languages> getLanguageByKey(String key) {
        Optional<Languages> language = Arrays.stream(Languages.values())
                .filter(lng -> lng.key.equals(key)).findAny();
        return language;
    }
}