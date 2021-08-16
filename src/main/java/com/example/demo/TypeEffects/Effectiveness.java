package com.example.demo.TypeEffects;

public enum Effectiveness {
    IMMUN(0), RESISTANT((float) 0.5), NORMAL(1), VERY(2);

    public final float value;

    Effectiveness(float value) {
        this.value = value;
    }
}
