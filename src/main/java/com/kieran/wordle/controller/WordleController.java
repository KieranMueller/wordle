package com.kieran.wordle.controller;

import com.kieran.wordle.entity.Wordle;
import com.kieran.wordle.service.WordleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class WordleController {

    private final WordleService wordleService;

    @GetMapping("wordles/users/{id}")
    public List<Wordle> getAllWordlesCreatedByUser(@PathVariable Long id) {
        return wordleService.getAllWordlesCreatedByUser(id);
    }

    @PostMapping("wordle")
    public Wordle createWordle(@RequestBody Wordle wordle) {
        return wordleService.createWordle(wordle);
    }

    @PostMapping("free-wordle")
    public HashMap<String, String> createWordleWithNoOwner(@RequestBody Wordle wordle) {
        return wordleService.createWordleWithNoOwner(wordle);
    }

    @GetMapping("free-wordle/{uuid}")
    public Wordle getWordleByUuidLink(@PathVariable UUID uuid) {
        return wordleService.getWordleByUuidLink(uuid);
    }

    @DeleteMapping("free-wordle/{uuid}")
    public Wordle deleteWordleByUuidLink(@PathVariable UUID uuid) {
        return wordleService.deleteWordleByUuidLink(uuid);
    }

    @DeleteMapping("wordle/{id}")
    public Wordle deleteWordle(Long id) {
        return wordleService.deleteWordle(id);
    }

}
