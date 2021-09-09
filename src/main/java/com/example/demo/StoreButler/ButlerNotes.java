package com.example.demo.StoreButler;

import com.example.demo.Combat.PhraseStore.PhraseTable;
import com.example.demo.TypeEffects.TypeTable;

public class ButlerNotes {
    String[] attacks;
    TypeTable types;
    PhraseTable phrases;

    ButlerNotes() {
    }

    ButlerNotes(String[] attacks, TypeTable types, PhraseTable phrases) {
        this.attacks = attacks;
        this.phrases = phrases;
        this.types = types;
    }
}