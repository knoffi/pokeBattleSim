package com.example.demo.Combat;

public class CombatResult {
    public String winnerName;
    public String[] commentary;

    CombatResult(String winnerName, String[] commentary) {
        this.commentary = commentary;
        this.winnerName = winnerName;
    }
}