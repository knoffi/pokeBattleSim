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
}