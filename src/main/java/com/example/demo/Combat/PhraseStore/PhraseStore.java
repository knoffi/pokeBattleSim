package com.example.demo.Combat.PhraseStore;

import com.example.demo.StoreButler.StoreButler;
import com.example.demo.StoreButler.StoreButlerServices;
import com.example.demo.TypeEffects.Effectiveness;

public class PhraseStore {
    static private PhraseTable PHRASES = getPhrases();

    public static PhraseTable getUpdatedTable() {
        PhraseTable newTable = new PhraseTable();
        newTable.updateTranslations();
        return newTable;
    }

    private static PhraseTable getPhrases() {
        try {
            PhraseTable table = StoreButler.getData(StoreButlerServices.PHRASES, PhraseTable.class);
            return table;
        } catch (Exception e) {
            System.out.print("___GET-REQUEST FOR PHRASE TABLE FAILED___" + e.getClass());
        }
        return getUpdatedTable();

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

    public static String getSummonPhrase(Languages language, boolean blueSummons) {
        return PHRASES.getSummonPhrase(language, blueSummons);
    }

    public static String getStatPhrase(Languages language, boolean isRising) {
        return PHRASES.getStatPhrase(language, isRising);
    }

    public static String getSpeedDiffPhrase(Languages language, int speedDiff) {
        return PHRASES.getSpeedDiffPhrase(language, speedDiff);
    }

}
