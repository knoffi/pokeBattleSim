package com.example.demo.TypeEffects;

public enum Effectiveness {
    IMMUN(Double.NEGATIVE_INFINITY), RESISTANT(-1), NORMAL(0), VERY(1), SUPER(2), SUPER_BAD(-2);

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
                System.out.println("___EFFECT VALUE NOT FOUND___" + e.getClass());
            }
            return NORMAL;
        }
    }
}
