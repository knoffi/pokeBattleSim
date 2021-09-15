package com.example.demo.Pokemon.Status;

public enum StatusKeys {
    PARA("paralysis"), SLEEP("sleep"), BURN("burn"), POISON("poison"), CONFUS("confusion"), FREEZE("freeze"),
    NONE("none");

    public final String name;

    private StatusKeys(String name) {
        this.name = name;
    }
}