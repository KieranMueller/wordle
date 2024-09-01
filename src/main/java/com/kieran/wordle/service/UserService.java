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

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WordleRepository wordleRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;

    public UserResponseDto findUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCaseAndConfirmedEmailTrue(username);
        if (user == null) throw new NotFoundException("Unable to find user with username " + username);
        else return UserResponseDto.mapUserToUserResponseDto(user);
    }

    public UserResponseDto validateLogin(LoginModel loginModel) {
        User user = userRepository.findByUsernameIgnoreCase(loginModel.getUsername());
        if (user != null && encoder.matches(loginModel.getPassword(), user.getPassword())) {
            if (user.getConfirmedEmail() && loginModel.getEmailUuid().isBlank()) {
                return UserResponseDto.mapUserToUserResponseDto(user);
            } else if (!user.getConfirmedEmail() && Objects.equals(user.getEmailUuid(), loginModel.getEmailUuid())) {
                user.setConfirmedEmail(true);
                user.setEmailUuid(UUID.randomUUID().toString());
                userRepository.saveAndFlush(user);
                return UserResponseDto.mapUserToUserResponseDto(user);
            } else throw new BadRequestException("Check email to login");
        } else throw new BadRequestException("Bad credentials");
    }

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

    public UserResponseDto createNewPassword(NewPasswordRequest newPasswordRequest) {
        User user = userRepository.findByResetPasswordUuid(newPasswordRequest.getResetPasswordUuid());
        validateUser(user);
        String newEmailUuid = UUID.randomUUID().toString();
        String newResetPasswordUuid = UUID.randomUUID().toString();
        user.setEmailUuid(newEmailUuid);
        user.setResetPasswordUuid(newResetPasswordUuid);
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

    public UserResponseDto updateUser(User user) {
        Optional<User> opUser = userRepository.findById(user.getId());
        if (opUser.isEmpty()) throw new NotFoundException("Unable to find user with ID: " + user.getId());
        User updatedUser = updateUserFields(user, opUser.get());
        User savedUser = userRepository.save(updatedUser);
        return UserResponseDto.mapUserToUserResponseDto(savedUser);
    }

    @Transactional
    public UserResponseDto deleteUser(Long id) {
        Optional<User> opUser = userRepository.findById(id);
        if (opUser.isEmpty()) throw new BadRequestException("Unable to find user with ID: " + id);
        wordleRepository.deleteAllByOwnerId(id);
        userRepository.deleteById(id);
        return UserResponseDto.mapUserToUserResponseDto(opUser.get());
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

    private void validateUser(User user) {
        if (user == null || !user.getConfirmedEmail()) throw new NotFoundException("Unable to verify user");
    }

    private void handleNewUserEmail(User user) {
        try {
            emailService.sendEmail(user.getEmail(),
                    "Wordle by Kieran - User Confirmation!",
                    "Please Click This Link to Login\nhttp://localhost:4200/login/"
                            + user.getUsername() + "/" + user.getEmailUuid());
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
