package com.example.demo.Combat;

import java.util.Optional;

class AttackLog extends CombatLog {

    AttackLog(boolean blueActs, String message, Optional<String> type) {
        this.blueActs = blueActs;
        this.attackPokeType = type.isPresent() ? type.get() : "statusChange";
        this.message = message;
        this.type = CombatLogType.ATTACK.type;
    }
}