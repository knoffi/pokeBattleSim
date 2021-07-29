package com.example.demo.Trainer;

import com.example.demo.Pokemon.Pokemon;

public class Trainer {
    public Pokemon[] team;
    public String name;

    public Trainer(String name, Pokemon[] team) {
        this.team = team;
        this.name = name;
    }
}
