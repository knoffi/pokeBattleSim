package com.example.demo.Pokemon;

public class Stats {
    private Stat hp;
    private Stat attack;
    private Stat defense;
    private Stat specAttack;
    private Stat specDefense;
    private Stat speed;
    private Stat accurancy;

    Stats(Stat hp, Stat attack, Stat defense, Stat specAttack, Stat specDefense, Stat speed) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specAttack = specAttack;
        this.specDefense = hp;
        this.speed = speed;
    }

}

class Stat {
    String name;
    int value;
}