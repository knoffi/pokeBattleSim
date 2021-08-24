package com.example.demo.Combat.PhraseStore;

import java.util.Arrays;
import java.util.Optional;

import com.example.demo.TypeEffects.Effectiveness;

class PhraseTable {
    private PhraseRow[] rows;

    PhraseTable() {

    }

    public void updateTranslations() {
        this.rows = Arrays.stream(Languages.values()).map(language -> new PhraseRow(language))
                .toArray(PhraseRow[]::new);
    }

    public String getAttackPhrase(Languages language) {
        Optional<String> attackText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getAttackText()).findAny();

        if (attackText.isPresent()) {
            return attackText.get();
        } else {
            this.throwRowNotFound();
            return PhraseRow.defaultAttackText;
        }
    }

    public String getResultPhrase(Languages language) {
        Optional<String> resultText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getResultText()).findAny();

        if (resultText.isPresent()) {
            return resultText.get();
        } else {
            this.throwRowNotFound();
            return PhraseRow.defaultResultText;
        }
    }

    public String getEffectPhrase(Languages language, Effectiveness effect) {
        Optional<String> effectText = Arrays.stream(this.rows).filter(row -> row.belongsToLanguage(language))
                .map(row -> row.getEffectText(effect)).findAny();

        if (effectText.isPresent()) {
            return effectText.get();
        } else {
            this.throwRowNotFound();
            return PhraseRow.defaultEffectText;
        }
    }

    private void throwRowNotFound() {
        try {
            throw new Exception("LanguageRowNotFound");
        } catch (Exception e) {
            System.out.println("___NO ROW FOR DESIRED LANGUAGE___" + e.getClass());
        }
    }

}