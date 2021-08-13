package com.example.demo.TypeEffects;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

public class TypeStore {
    final static private String TYPE_FILE_PATH = "./pokeBattleSim/src/main/java/com/example/demo/TypeEffects/TypeTable.json";

    public static void updateTypes() {
        JSONObject test = new JSONObject();
        test.put("value", 12);
        try {
            FileWriter file = new FileWriter(TYPE_FILE_PATH);
            file.write(test.toJSONString());
            file.close();
        } catch (IOException e) {
            System.out.println("___WRITING TYPE TABLE FAILED___" + e.getClass());
        }

    };
}