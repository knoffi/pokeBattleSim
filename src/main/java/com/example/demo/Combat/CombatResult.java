package com.example.demo.Combat;

public class CombatResult {
    public String winnerName;
    public String[] commentary;
    public boolean blueWon;

    public CombatResult(String winnerName, String[] commentary, boolean blueWon) {
        this.commentary = commentary;
        this.winnerName = winnerName;
        this.blueWon = blueWon;
    }
}