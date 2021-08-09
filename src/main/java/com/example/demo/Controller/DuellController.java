package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DuellController {
    @GetMapping(value = "/getTrainerDuell")
    public @ResponseBody Test index() {
        return new Test("Hello");
    }
}

class Test {
    public String name;

    Test(String name) {
        this.name = name;
    }
}
