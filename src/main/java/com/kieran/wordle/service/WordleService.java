package com.kieran.wordle.service;

import com.kieran.wordle.entity.User;
import com.kieran.wordle.entity.Wordle;
import com.kieran.wordle.exception.BadRequestException;
import com.kieran.wordle.exception.NotAuthorizedException;
import com.kieran.wordle.repository.UserRepository;
import com.kieran.wordle.repository.WordleRepository;
import lombok.RequiredArgsConstructor;
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
    public Wordle createWordle(Wordle wordle) {
        Optional<User> opUser = userRepository.findById(wordle.getOwnerId());
        if (opUser.isEmpty()) throw new BadRequestException("Invalid user ID: " + wordle.getOwnerId());
        User user = opUser.get();
        user.setCurrentWordlesOwned(user.getCurrentWordlesOwned() + 1);
        if (user.getCurrentWordlesOwned() > 10) throw new NotAuthorizedException("User has too many wordles");
        return wordleRepository.save(wordle);
    }

    /*
    returns all wordles a user has created based on Wordle.ownerId. returns empty list if 0.
    TODO
     */
    public List<Wordle> getAllWordlesCreatedByUser(Long id) {
        return wordleRepository.findByOwnerIdIs(id);
    }

    /*
    first we find wordle by id, if doesn't exist, return 400. else: delete wordle in db, decrement the owner's wordle
    count by one, and return the wordle we deleted.
    TODO
    - need to verify that the user who owns this wordle is the current logged in user before deleting
     */
    public Wordle deleteWordle(Long id) {
        Optional<Wordle> opWordle = wordleRepository.findById(id);
        if (opWordle.isEmpty()) throw new BadRequestException("Could not find wordle with ID: " + id);
        Wordle wordle = opWordle.get();
        wordleRepository.deleteById(id);
        Optional<User> user = userRepository.findById(wordle.getOwnerId());
        user.ifPresent(thisUser -> thisUser.setCurrentWordlesOwned(thisUser.getCurrentWordlesOwned() - 1));
        return wordle;
    }
}
