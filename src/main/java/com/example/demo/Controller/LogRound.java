package com.example.demo.Controller;

import com.example.demo.Combat.CombatLog;

public class LogRound {
    public String redCombatant;
    public String blueCombatant;
    public CombatLog[] battleLog;
    public boolean blueWon;

    public LogRound(String redCombatant, String blueCombatant, CombatLog[] battleLog, boolean blueWon) {
        this.redCombatant = redCombatant;
        this.blueCombatant = blueCombatant;
        this.battleLog = battleLog;
        this.blueWon = blueWon;
    }

}
