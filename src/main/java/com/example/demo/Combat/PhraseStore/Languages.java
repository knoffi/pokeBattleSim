package com.example.demo.Combat.PhraseStore;

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
}