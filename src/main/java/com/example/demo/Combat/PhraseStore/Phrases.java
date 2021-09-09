package com.example.demo.Combat.PhraseStore;

public enum Phrases {
    attack("XXX used YYY."), result("XXX was defeated!"), normalEffect(""), veryEffect("It was very effective!"),
    superEffect("It was super effective!"), notVeryEffect("It was not very effective!"),
    veryBadEffect("It was nearly ineffective!"), immunEffect("Nothing happens!"), summon("XXX sent out YYY!"),
    statFall("XXX's YYY falls!"), statRise("XXX's YYY rises!"), speedDiff0("Both are equally fast!"),
    speedDiff1("XXX is slightly faster!"), speedDiff2("XXX is faster!"), speedDiff3("XXX is much faster!"),
    speedDiff4("XXX is extremely faster!");

    public final String text;

    Phrases(String text) {
        this.text = text;
    }
}