package com.example.demo.Combat.Logs;

public enum CombatLogType {
    ATTACK("attack"), TEXT("text"), FAINT("faint"), SUMMON("summon");

    public final String type;

    private CombatLogType(String type) {
        this.type = type;
    }
}
