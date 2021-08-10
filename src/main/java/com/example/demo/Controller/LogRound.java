package com.example.demo.Controller;

public class LogRound {
    public String redCombatant;
    public String blueCombatant;
    public String[] battleLog;
    public boolean blueWon;

    public LogRound(String redCombatant, String blueCombatant, String[] battleLog, boolean blueWon) {
        this.redCombatant = redCombatant;
        this.blueCombatant = blueCombatant;
        this.battleLog = battleLog;
        this.blueWon = blueWon;
    }
}
