package com.example.demo.Controller;

import java.util.Arrays;
import java.util.Optional;

import com.example.demo.Combat.PhraseStore.Languages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DuellController {
    @Autowired
    private DuellLogFactory provider;

    @GetMapping(value = "/getTrainerDuell")
    public @ResponseBody DuellLog index(@RequestParam(defaultValue = "en") String lng) {
        Languages language = this.getLanguageFromQuery(lng);
        return this.provider.get(language);
    }

    private Languages getLanguageFromQuery(String lng) {
        Optional<Languages> queryLanguage = Arrays.stream(Languages.values())
                .filter(language -> language.key.equals(lng)).findAny();
        if (queryLanguage.isPresent()) {
            return queryLanguage.get();
        } else {
            try {
                throw new Exception("LanguageKeyNotFound");
            } catch (Exception e) {
                System.out.println("___FAIL AT LANGUAGE QUERY___" + e.getClass());
            }
            return Languages.DE;
        }

    }
}