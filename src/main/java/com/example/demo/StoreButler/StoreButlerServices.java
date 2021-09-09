package com.example.demo.StoreButler;

public enum StoreButlerServices {
    ATTACKS("attacks"), TYPES("types"), PHRASES("phrases");

    public final String key;

    StoreButlerServices(String key) {
        this.key = key;
    }
}