package com.example.demo.Pokemon;

public class PokeStats {
    private int hp;
    private int attack;
    private int defense;
    private int specAttack;
    private int specDefense;
    private int speed;
    private PokeType[] types;

    PokeStats(int hp, int attack, int defense, int specAttack, int specDefense, int speed, PokeType[] types) {
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specAttack = specAttack;
        this.specDefense = hp;
        this.speed = speed;
        this.types = types;
    }

}