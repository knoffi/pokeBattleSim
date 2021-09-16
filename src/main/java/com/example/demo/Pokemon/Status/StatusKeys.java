package com.example.demo.Pokemon.Status;

public enum StatusKeys {
    PARA("paralysis"), SLEEP("sleep"), BURN("burn"), POISON("poison"), CONFUS("confusion"), FREEZE("freeze"),
    NONE("none");

    public final String name;

    private StatusKeys(String name) {
        this.name = name;
    }

    public static StatusKeys getKeyFromName(String name) {
        switch (name) {
            case "paralysis":
                return StatusKeys.PARA;
            case "sleep":
                return StatusKeys.SLEEP;
            case "burn":
                return StatusKeys.BURN;
            case "poison":
                return StatusKeys.POISON;
            case "confusion":
                return StatusKeys.CONFUS;
            case "freeze":
                return StatusKeys.FREEZE;
            case "none":
                return StatusKeys.NONE;

            default:
                try {
                    throw new Exception("StatusKeyNotFound");
                } catch (Exception e) {
                    System.out.println("___STATUS KEY NOT FOUND FROM NAME___");
                }
                break;
        }
        return StatusKeys.BURN;
    }
}