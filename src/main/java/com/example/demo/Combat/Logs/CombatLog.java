package com.example.demo.Combat.Logs;

import com.example.demo.Combat.PhraseStore.Languages;

//TODO: make fields private and configure the object mapper of Spring Boot for the JSON response
public abstract class CombatLog {
    public boolean blueActs;
    public String type;
    public String message;
    public String attackPokeType;

    protected boolean isEuropean(Languages language) {
        if (language == Languages.JA || language == Languages.KO || language == Languages.ZH) {
            return false;
        }
        return true;
    }

}