package com.kieran.wordle.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${sample-greeting}")
    private String greeting;

    @Value("${sample-url:oops}")
    private String url;

    @Value("${sample-username:uh-oh}")
    private String username;

    @GetMapping("/greeting")
    public String greeting() {
        return greeting + "-" + url + "-" + username;
    }
}
