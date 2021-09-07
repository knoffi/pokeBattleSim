package com.example.demo.Combat;

class ResultLog extends CombatLog {
    ResultLog(boolean blueWins, String name) {
        // front end needs this field
        this.blueActs = blueWins;
        this.type = CombatLogType.FAINT.type;
        this.message = this.createMessage(name);
    }

    private String createMessage(String name) {
        return name + " was defeated!";
    }
}