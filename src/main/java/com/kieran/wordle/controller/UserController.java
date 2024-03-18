package com.kieran.wordle.controller;

import com.kieran.wordle.dto.UserResponseDto;
import com.kieran.wordle.entity.User;
import com.kieran.wordle.model.ForgotPasswordRequest;
import com.kieran.wordle.model.LoginModel;
import com.kieran.wordle.model.NewPasswordRequest;
import com.kieran.wordle.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("users/{username}")
    public UserResponseDto findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    @PostMapping("login")
    public UserResponseDto isValidUser(@RequestBody LoginModel loginModel) {
        return userService.validateLogin(loginModel);
    }

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createNewUser(@RequestBody User user) {
        return userService.createNewUser(user);
    }

    @PostMapping("forgot-password")
    public UserResponseDto forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return userService.forgotPassword(forgotPasswordRequest);
    }

    @PostMapping("create-new-password")
    public UserResponseDto createNewPassword(@RequestBody NewPasswordRequest newPasswordRequest) {
        return userService.createNewPassword(newPasswordRequest);
    }

    @GetMapping("confirm-password-change/{emailUuid}")
    public String confirmPasswordChange(@PathVariable String emailUuid) {
        return userService.confirmPasswordChange(emailUuid);
    }

    @PatchMapping("update")
    public UserResponseDto updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("delete/users/{id}")
    public UserResponseDto deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

}
