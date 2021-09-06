package com.example.demo.Pokemon;

public enum StatKeys {
    ATT("attack"), SPEC_ATT("special-attack"), DEF("defense"), SPEC_DEF("special-defense"), HP("hp"), SPEED("speed");

    public final String name;

    StatKeys(String name) {
        this.name = name;
    }
}