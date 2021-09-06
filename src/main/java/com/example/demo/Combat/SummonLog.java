package com.example.demo.Combat;

class SummonLog extends CombatLog {
    SummonLog(boolean blueActs, String pokemonName) {
        this.blueActs = blueActs;
        this.type = CombatLogType.SUMMON.type;
        this.message = this.getMessage(pokemonName);
    }

    private String getMessage(String pokemonName) {
        String actor = this.blueActs ? "BLUE" : "RED";
        return actor + " send out " + pokemonName + "!";

    }
}