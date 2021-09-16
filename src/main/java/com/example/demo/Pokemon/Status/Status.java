package com.example.demo.Pokemon.Status;

public class Status {
    public static int SLEEP_ROUNDS = 3;
    public static int POISON_ROUNDS = 21;
    public StatusKeys key;
    public int roundsLeft;
    public double dotDamage;
    public double selfHarm;
    public double damageReduce;

    public Status() {
        this.key = StatusKeys.NONE;
        this.roundsLeft = 300;
        this.dotDamage = 0;
        this.selfHarm = 0;
        this.damageReduce = 0;

    }

    public Status(String name) {
        boolean statusWasNotFound = true;
        if (name.equals(StatusKeys.BURN.name)) {
            this.key = StatusKeys.NONE;
            this.roundsLeft = 300;
            this.dotDamage = 0.05;
            this.selfHarm = 0;
            this.damageReduce = 0.05;
            statusWasNotFound = false;
        }
        if (name.equals(StatusKeys.POISON.name)) {
            this.key = StatusKeys.NONE;
            this.roundsLeft = 300;
            this.dotDamage = 0.1;
            this.selfHarm = 0;
            this.damageReduce = 0;
            statusWasNotFound = false;
        }
        if (name.equals(StatusKeys.CONFUS.name)) {
            this.key = StatusKeys.NONE;
            this.roundsLeft = 3;
            this.dotDamage = 0;
            this.selfHarm = 0.1;
            this.damageReduce = 0;
            statusWasNotFound = false;
        }
        if (name.equals(StatusKeys.SLEEP.name)) {
            this.key = StatusKeys.NONE;
            this.roundsLeft = 3;
            this.dotDamage = 0;
            this.selfHarm = 0;
            this.damageReduce = 1;
            statusWasNotFound = false;
        }
        if (name.equals(StatusKeys.FREEZE.name)) {
            this.key = StatusKeys.NONE;
            this.roundsLeft = 3;
            this.dotDamage = 0;
            this.selfHarm = 0;
            this.damageReduce = 1;
            statusWasNotFound = false;
        }
        if (name.equals(StatusKeys.PARA.name)) {
            this.key = StatusKeys.NONE;
            this.roundsLeft = 3;
            this.dotDamage = 0;
            this.selfHarm = 0;
            this.damageReduce = 1;
            statusWasNotFound = false;
        }
        if (statusWasNotFound) {
            try {
                throw new Exception("PokeStatusNotFound");
            } catch (Exception e) {
                System.out.println("___STATUS " + name + " WAS NOT FOUND");
            }
        }
    }
}