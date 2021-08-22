package com.example.demo.Translater;

import java.util.HashMap;

class TranslationKeyMapper {
    // mapping for Pokemon API
    private static HashMap<String, String> keyMap = new HashMap<String, String>();
    static {
        keyMap.put("en", "en");
        keyMap.put("de", "de");
    }

    public static String getKey(String languageParam) {
        return keyMap.get(languageParam);
    }
}