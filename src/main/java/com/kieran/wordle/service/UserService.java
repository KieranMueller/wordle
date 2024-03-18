package com.kieran.wordle.service;

import com.kieran.wordle.dto.UserResponseDto;
import com.kieran.wordle.entity.User;
import com.kieran.wordle.exception.BadRequestException;
import com.kieran.wordle.exception.NotFoundException;
import com.kieran.wordle.model.ForgotPasswordRequest;
import com.kieran.wordle.model.LoginModel;
import com.kieran.wordle.model.NewPasswordRequest;
import com.kieran.wordle.repository.UserRepository;
import com.kieran.wordle.repository.WordleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    public String loginViaEmailUuid(String uuid) {
        User user = userRepository.findByEmailUuid(uuid);
        if (user == null) throw new BadRequestException("Something went wrong");
        user.setConfirmedEmail(true);
        userRepository.save(user);
        return "Good to go " + user.getUsername() + "! Please revisit the site and sign in";
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
        handleNewUserEmail(user);
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return UserResponseDto.mapUserToUserResponseDto(savedUser);
    }

    public UserResponseDto forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmailOrUsernameIgnoreCase(
                forgotPasswordRequest.getEmail(), forgotPasswordRequest.getUsername());
        validateUser(user);
        resetPasswordEmail(user);
        return UserResponseDto.mapUserToUserResponseDto(user);
    }

    // setting new email uuid to make things more secure, in the final email confirm, we need to find this user by the
    // new email uuid, then set the new password etc.
    public UserResponseDto createNewPassword(NewPasswordRequest newPasswordRequest) {
        User user = userRepository.findByResetPasswordUuid(newPasswordRequest.getResetPasswordUuid());
        validateUser(user);
        String newUuid = UUID.randomUUID().toString();
        user.setEmailUuid(newUuid);
        user.setNewPassword(encoder.encode(newPasswordRequest.getPassword()));
        User savedUser = userRepository.save(user);
        confirmPasswordResetEmail(savedUser);
        return UserResponseDto.mapUserToUserResponseDto(user);
    }

    public String confirmPasswordChange(String emailUuid) {
        User user = userRepository.findByEmailUuid(emailUuid);
        validateUser(user);
        user.setPassword(user.getNewPassword());
        userRepository.save(user);
        return "Password change success! You may close this tab.";
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

    private void validateUser(User user) {
        if (user == null || !user.isConfirmedEmail()) throw new NotFoundException("Unable to verify user");
    }

    /*
    When user is created (createNewUser method), user receives email sent to User.email. Email should route user to
    home page of app and log them in automatically.
    TODO
    - Unfinished
    -  Link will direct you to front end, link will need to have dynamic encrypted word or uuid in it
        so that when user clicks it, they're immediately taken to front end and logged in automatically
     */
    private void handleNewUserEmail(User user) {
        try {
            emailService.sendEmail(user.getEmail(),
                    "Wordle by Kieran - User Confirmation!",
                    "Please Click This Link to Login\nhttp://localhost:8080/email/login/" + user.getEmailUuid());
        } catch (Exception e) {
            throw new NotFoundException("Invalid email: " + user.getEmail());
        }
    }

    private void resetPasswordEmail(User user) {
        try {
            emailService.sendEmail(user.getEmail(),
                    "Wordle by Kieran - Reset Password Request",
                    "Please click this link to reset your password\nhttp://localhost:4200/set-password/" + user.getResetPasswordUuid());
        } catch (Exception e) {
            throw new NotFoundException("Invalid email: " + user.getEmail());
        }
    }

    private void confirmPasswordResetEmail(User user) {
        try {
            emailService.sendEmail(user.getEmail(),
                    "Wordle by Kieran - Reset Password Confirmation!",
                    "Success! Please Click This Link to Confirm Password Change\nhttp://localhost:8080/confirm-password-change/" + user.getEmailUuid());
        } catch (Exception e) {
            throw new NotFoundException("Invalid email: " + user.getEmail());
        }
    }
}
