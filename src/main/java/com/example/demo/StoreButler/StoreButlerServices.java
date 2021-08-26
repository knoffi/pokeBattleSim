package com.example.demo.StoreButler;

public enum StoreButlerServices {
    ATTACKS("attacks"), TYPES("typeTable"), PHRASES("phraseTable");

    public final String key;

    StoreButlerServices(String key) {
        this.key = key;
    }
}