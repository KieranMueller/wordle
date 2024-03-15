package com.kieran.wordle.controller;

import com.kieran.wordle.entity.Wordle;
import com.kieran.wordle.service.WordleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordleController {

    private final WordleService wordleService;

    @GetMapping("wordles/users/{id}")
    public ResponseEntity<List<Wordle>> getAllWordlesCreatedByUser(@PathVariable Long id) {
        return wordleService.getAllWordlesCreatedByUser(id);
    }

    @PostMapping("wordle")
    public ResponseEntity<String> createWordle(@RequestBody Wordle wordle) {
        wordle.setWord(wordle.getWord().toLowerCase());
        // Handle field validation on front end
        // Need JWT in request body so somebody doesn't just spam postman
        return wordleService.createWordle(wordle);
    }

    @DeleteMapping("wordle/{id}")
    public ResponseEntity<String> deleteWordle(Long id) {
        return wordleService.deleteWordle(id);
    }

}
