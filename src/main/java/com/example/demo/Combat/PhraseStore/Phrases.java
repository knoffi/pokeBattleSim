package com.example.demo.Combat.PhraseStore;

enum Phrases {
    attack("XXX used YYY."), result("XXX was defeated!"), normalEffect(""), veryEffect("It was very effective!"),
    superEffect("It was super effective!"), notVeryEffect("It was not very effective!"),
    veryBadEffect("It was nearly ineffective!"), immunEffect("Nothing happens!");

    public final String text;

    Phrases(String text) {
        this.text = text;
    }
}