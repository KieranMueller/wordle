package com.kieran.wordle.service;

import com.kieran.wordle.entity.User;
import com.kieran.wordle.model.LoginModel;
import com.kieran.wordle.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public Boolean validateLogin(LoginModel loginModel) {
        return userRepository.findByUsernameAndPasswordAndConfirmedEmailTrue(
                loginModel.getUsername(), loginModel.getPassword()) != null;
    }

    public ResponseEntity<String> createNewUser(@Valid User user) {
        // handle validation errors
        // implement email service (not finished!!)
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()) != null)
            return new ResponseEntity<>("Username Already Exists", HttpStatus.BAD_REQUEST);
        if (userRepository.findByEmailIgnoreCase(user.getEmail()) != null)
            return new ResponseEntity<>("Email Already Exists", HttpStatus.BAD_REQUEST);
        User savedUser = userRepository.save(user);
        handleEmail(user);
        return new ResponseEntity<>("Saved User With ID: " + savedUser.getId() + ". Check Email for Confirmation",
                HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateUser(User user) {
        Optional<User> opUser = userRepository.findById(user.getId());
        if (opUser.isEmpty()) return new ResponseEntity<>(
                "Unable to Find User With ID: " + user.getId(), HttpStatus.BAD_REQUEST);
        User updatedUser = updateUserFields(user, opUser.get());
        userRepository.save(updatedUser);
        return new ResponseEntity<>("Updated User", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUser(Long id) {
        Optional<User> opUser = userRepository.findById(id);
        if (opUser.isEmpty()) return new ResponseEntity<>("Unable to Find User with ID: " + id,
                HttpStatus.BAD_REQUEST);
        // Delete all of the user's wordles here too!
        userRepository.deleteById(id);
        return new ResponseEntity<>("Deleted User with ID " + id, HttpStatus.OK);
    }

    private User updateUserFields(User updatedUser, User existingUser) {
        if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isBlank())
            existingUser.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null && !updatedUser.getLastName().isBlank())
            existingUser.setLastName(updatedUser.getLastName());
        if (updatedUser.getPhone() != null && !updatedUser.getPhone().isBlank())
            existingUser.setPhone(updatedUser.getPhone());
        return existingUser;
    }

    private void handleEmail(User user) {
        // Unfinished
        emailService.sendEmail("kieran98mueller@gmail.com",
                "Wordle by Kieran User Confirmation!",
                "Please Click This Link to Login\nhttps://www.youtube.com/watch?v=BzGm2NtyfE4&t=176s");
    }
}
