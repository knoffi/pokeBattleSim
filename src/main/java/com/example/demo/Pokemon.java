package com.example.demo;

public class Pokemon {
    public String name;
    private String url;

    public Pokemon() {
        this.name = "Bulbasaur";
        this.url = "https://pokeapi.co/api/v2/pokemon/1/";
    }

    public Pokemon(String name, String url) {
        this.name = name;
        this.url = url;
    }
}