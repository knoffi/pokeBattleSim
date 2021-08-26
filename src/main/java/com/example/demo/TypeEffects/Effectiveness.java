package com.example.demo.TypeEffects;

public enum Effectiveness {
    IMMUN(0), RESISTANT(0.5), NORMAL(1), VERY(2), SUPER(4), SUPER_BAD(0.25);

    public final double value;

    Effectiveness(double value) {
        this.value = value;
    }

    public static Effectiveness findKeyFromValue(double value) {
        if (value == Effectiveness.IMMUN.value) {
            return IMMUN;
        }
        if (value == Effectiveness.RESISTANT.value) {
            return RESISTANT;
        }
        if (value == Effectiveness.NORMAL.value) {
            return NORMAL;
        }
        if (value == Effectiveness.VERY.value) {
            return VERY;
        }
        if (value == Effectiveness.SUPER.value) {
            return SUPER;
        }
        if (value == Effectiveness.SUPER_BAD.value) {
            return SUPER_BAD;
        } else {
            try {
                throw new Exception("EffectValueNotFound");
            } catch (Exception e) {
                System.out.println("___EFFECT VALUE NOT FOUND" + value + "___" + e.getClass());
            }
            return NORMAL;
        }
    }
}
