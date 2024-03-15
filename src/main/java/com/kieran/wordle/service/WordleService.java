package com.kieran.wordle.service;

import com.kieran.wordle.entity.Wordle;
import com.kieran.wordle.repository.UserRepository;
import com.kieran.wordle.repository.WordleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordleService {

    private final WordleRepository wordleRepository;
    private final UserRepository userRepository;

    public ResponseEntity<String> createWordle(Wordle wordle, Long userId) {

        return null;
    }
}
