package com.example.demo.Combat.Logs;

public class SpeedContestLog extends CombatLog {
    public SpeedContestLog(boolean blueActs, String winner, int stepsAhead) {
        // front end needs this field
        this.blueActs = blueActs;
        this.type = CombatLogType.TEXT.type;
        this.message = this.createMessage(winner, stepsAhead);
    }

    private String createMessage(String winner, int stepsAhead) {
        if (stepsAhead == 0) {
            return "Nobody was faster!";
        }

        String speedDiffPhrase;

        switch (stepsAhead) {
            case 1:
                speedDiffPhrase = " slightly";
                break;
            case 2:
                speedDiffPhrase = "";
                break;
            case 3:
                speedDiffPhrase = " much";
                break;
            default:
                speedDiffPhrase = " extremely";
                break;
        }

        String message = winner + " is" + speedDiffPhrase + " faster!";
        return message;
    }
}