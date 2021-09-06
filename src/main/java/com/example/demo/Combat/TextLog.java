package com.example.demo.Combat;

class TextLog extends CombatLog {
    TextLog(boolean blueActs, String message) {
        this.blueActs = blueActs;
        this.message = message;
        this.type = CombatLogType.TEXT.type;
    }
}