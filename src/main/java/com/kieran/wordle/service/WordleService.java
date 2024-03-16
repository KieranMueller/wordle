package com.kieran.wordle.service;

import com.kieran.wordle.entity.User;
import com.kieran.wordle.entity.Wordle;
import com.kieran.wordle.repository.UserRepository;
import com.kieran.wordle.repository.WordleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordleService {

    private final WordleRepository wordleRepository;
    private final UserRepository userRepository;

    /*
    Here we first find the user who created this wordle by Wordle.ownerId. If user does not exist, return 400.
    else: increment the users current wordles owned, then, if user has more than 10 wordles, return forbidden. Else,
    return 201 with created wordle ID.
    TODO
     */
    public ResponseEntity<String> createWordle(Wordle wordle) {
        Optional<User> opUser = userRepository.findById(wordle.getOwnerId());
        if (opUser.isEmpty())
            return new ResponseEntity<>("Invalid User ID: " + wordle.getOwnerId(), HttpStatus.BAD_REQUEST);
        User user = opUser.get();
        user.setCurrentWordlesOwned(user.getCurrentWordlesOwned() + 1);
        if (user.getCurrentWordlesOwned() > 10)
            return new ResponseEntity<>("User has too many wordles", HttpStatus.FORBIDDEN);
        Wordle savedWordle = wordleRepository.save(wordle);
        return new ResponseEntity<>("Saved Wordle With ID " + savedWordle.getId(), HttpStatus.CREATED);
    }

    /*
    returns all wordles a user has created based on Wordle.ownerId. returns empty list if 0.
    TODO
     */
    public ResponseEntity<List<Wordle>> getAllWordlesCreatedByUser(Long id) {
        return new ResponseEntity<>(wordleRepository.findByOwnerIdIs(id), HttpStatus.OK);
    }

    /*
    first we find wordle by id, if doesn't exist, return 400. else: delete wordle in db and return wordle id.
    TODO
    - need to verify that the user who owns this wordle is the current logged in user before deleting
     */
    public ResponseEntity<String> deleteWordle(Long id) {
        Optional<Wordle> opWordle = wordleRepository.findById(id);
        if (opWordle.isEmpty())
            return new ResponseEntity<>("Could Not Find Wordle With ID " + id, HttpStatus.BAD_REQUEST);
        wordleRepository.deleteById(id);
        return new ResponseEntity<>("Deleted Wordle With ID " + id, HttpStatus.OK);
    }
}
