package com.example.demo.Controller;

import com.example.demo.Combat.PhraseStore.Languages;
import com.example.demo.Pokemon.Pokemon;
import com.example.demo.TrainerDuell.TrainerDuell;

import org.springframework.stereotype.Component;

@Component
class DuellFactory {
    public LogRound[] get(Pokemon[] red, Pokemon[] blue, Languages lng) {
        TrainerDuell duell = new TrainerDuell(red, blue);
        LogRound[] rounds = duell.letThemFight(lng);
        return rounds;
    }
}
