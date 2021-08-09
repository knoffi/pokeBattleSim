package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DuellController {
    @GetMapping("/1")
    public String index() {
        return "Hello";
    }
}
