package com.kieran.wordle.controller;

import com.kieran.wordle.entity.Wordle;
import com.kieran.wordle.service.WordleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WordleController {

    private final WordleService wordleService;

    @GetMapping("wordles/users/{id}")
    public ResponseEntity<Wordle> getAllWordlesCreatedByUser(@PathVariable Long id) {
        return null;
    }

    @PostMapping("wordle")
    public ResponseEntity<String> createWordle(@RequestBody Wordle wordle, @RequestBody Long userId) {
        return wordleService.createWordle(wordle, userId);
    }

}
