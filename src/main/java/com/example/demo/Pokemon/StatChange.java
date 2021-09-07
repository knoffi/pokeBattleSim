package com.example.demo.Pokemon;

public class StatChange {
    int value;
    String stat;

    public StatChange(int value, String stat) {
        this.value = value;
        this.stat = stat;
    }

    public boolean isRisingStat() {
        return this.value > 0;
    }

    public String getName() {
        return this.stat;
    }
}
