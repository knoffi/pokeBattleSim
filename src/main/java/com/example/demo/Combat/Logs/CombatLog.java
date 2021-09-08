package com.example.demo.Combat.Logs;

//TODO: make fields private and configure the object mapper of Spring Boot for the JSON response
public abstract class CombatLog {
    public boolean blueActs;
    public String type;
    public String message;
    public String attackPokeType;

}