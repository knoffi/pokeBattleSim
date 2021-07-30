package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GameController {

    GameController() {
        super();
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/game")
    String all() {
        return "hello peter";
    }
    // end::get-aggregate-root[]

}