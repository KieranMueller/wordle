package com.kieran.wordle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtherController {

    @GetMapping("")
    public String replaceWhiteLabel() {
        return "Welcome to the Wordle By Kieran API. Visit the site at https://wordle.kieranmueller.com or " +
                "view API docs at the /swagger-ui.html endpoint";
    }
}
