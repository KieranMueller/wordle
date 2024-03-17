package com.kieran.wordle.service;

import com.kieran.wordle.dto.UserResponseDto;
import com.kieran.wordle.entity.User;
import com.kieran.wordle.exception.BadRequestException;
import com.kieran.wordle.exception.NotFoundException;
import com.kieran.wordle.model.LoginModel;
import com.kieran.wordle.repository.UserRepository;
import com.kieran.wordle.repository.WordleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WordleRepository wordleRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;

    /*
    We want to find a user by username. User found with username (ignoring case) must have confirmed email,
    if so, we map the User found to a UserResponseObject to mask password, email, phone etc. and return user.
    TODO
     */
    public UserResponseDto findUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCaseAndConfirmedEmailTrue(username);
        if (user == null) throw new NotFoundException("Unable to find user with username " + username);
        else return UserResponseDto.mapUserToUserResponseDto(user);
    }

    /*
    Here we take in a loginModel (username and password), if username exists in DB, and the encoded password tied to
    the user with said username equals the request body password, we return true, else false.
    TODO
     */
    public Boolean validateLogin(LoginModel loginModel) {
        User user = userRepository.findByUsernameIgnoreCase(loginModel.getUsername());
        return (user != null && user.isConfirmedEmail() && encoder.matches(loginModel.getPassword(), user.getPassword()));
    }

    /*
    A user wants to create an account, we receive a user, we throw bad request if username or email ignore case
    already exists (since they'd be an existing user that shoud login normally). We encrypt their password then
    save the user in the DB. We call the handle email method (see method for explanation) then return 201.
    TODO
    - handle validation errors
    - implement email service (not finished!!)
     */
    public UserResponseDto createNewUser(User user) {
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()) != null)
            throw new BadRequestException("Username already exists");
        if (userRepository.findByEmailIgnoreCase(user.getEmail()) != null)
            throw new BadRequestException("Email already exists");
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
//        handleEmail(user);
        return UserResponseDto.mapUserToUserResponseDto(savedUser);
    }

    /*
    TODO
    - Unfinished, implement JWT here
     */
    public String authenticate(LoginModel loginModel) {
        if (!validateLogin(loginModel)) throw new BadRequestException("Invalid credentials");
        return null;
    }

    /*
    A user can update their first, last name, or phone ONLY in this method. If user doesn't exist in DB, return 400.
    Call updateUserFields method to map non blank fields sent in request body and update existing user object.
    Save user and return 200.
    TODO
     */
    public UserResponseDto updateUser(User user) {
        Optional<User> opUser = userRepository.findById(user.getId());
        if (opUser.isEmpty()) throw new NotFoundException("Unable to find user with ID: " + user.getId());
        User updatedUser = updateUserFields(user, opUser.get());
        User savedUser = userRepository.save(updatedUser);
        return UserResponseDto.mapUserToUserResponseDto(savedUser);
    }

    /*
    A user wants to delete their account, if they do not exist in DB, return 400
    else: we delete all wordles they created (based on Wordle.ownerId), delete user, and return 200.
    TODO
     */
    @Transactional
    public UserResponseDto deleteUser(Long id) {
        Optional<User> opUser = userRepository.findById(id);
        if (opUser.isEmpty()) throw new BadRequestException("Unable to find user with ID: " + id);
        wordleRepository.deleteAllByOwnerId(id);
        userRepository.deleteById(id);
        return UserResponseDto.mapUserToUserResponseDto(opUser.get());
    }

    /*
    Helper method to map first name, last name, and/or phone to user object for patch endpoint (update user)
    TODO
     */
    private User updateUserFields(User updatedUser, User existingUser) {
        if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isBlank())
            existingUser.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null && !updatedUser.getLastName().isBlank())
            existingUser.setLastName(updatedUser.getLastName());
        if (updatedUser.getPhone() != null && !updatedUser.getPhone().isBlank())
            existingUser.setPhone(updatedUser.getPhone());
        return existingUser;
    }

    /*
    When user is created (createNewUser method), user receives email sent to User.email. Email should route user to
    home page of app and log them in automatically.
    TODO
    - Unfinished
    -  Link will direct you to front end, link will need to have dynamic encrypted word or uuid in it
        so that when user clicks it, they're immediately taken to front end and logged in automatically
     */
    private void handleEmail(User user) {
        emailService.sendEmail(user.getEmail(),
                "Wordle by Kieran User Confirmation!",
                "Please Click This Link to Login\nhttps://www.youtube.com/watch?v=dQw4w9WgXcQ");
    }
}
