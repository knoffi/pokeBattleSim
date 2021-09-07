package com.example.demo.Combat;

public class StatChangeLog extends CombatLog {
    StatChangeLog(boolean blueActs, String target, String stat, boolean isRising) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(target, stat, isRising);
    }

    private String createMessage(String target, String stat, boolean isRising) {
        String effect = isRising ? " rose!" : " fell!";
        String effectPhrase = "'s " + stat + effect;
        String message = target + effectPhrase;
        return message;
    }
}