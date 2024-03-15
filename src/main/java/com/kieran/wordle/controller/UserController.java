package com.kieran.wordle.controller;

import com.kieran.wordle.entity.User;
import com.kieran.wordle.model.LoginModel;
import com.kieran.wordle.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("users/{username}")
    public ResponseEntity<String> findUserByUsername(@PathVariable String username) {
        return null;
    }

    @PostMapping("login")
    public Boolean isValidUser(@RequestBody LoginModel loginModel) {
        return userService.validateLogin(loginModel);
    }

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createNewUser(@RequestBody User user) {
        return userService.createNewUser(user);
    }

    @PatchMapping("update")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

}
