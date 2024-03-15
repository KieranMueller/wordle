package com.kieran.wordle.service;

import com.kieran.wordle.entity.User;
import com.kieran.wordle.model.LoginModel;
import com.kieran.wordle.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class UserServiceTest {
//
//    UserService userService;
//    UserRepository userRepositoryMock;
//
//    UserServiceTest() {
//        this.userRepositoryMock = Mockito.mock(UserRepository.class);
//        this.userService = new UserService(userRepositoryMock);
//    }
//
//    @Test
//    void validateLogin() {
//        Mockito.when(userRepositoryMock.findByUsernameAndPassword("Mack", "Jones"))
//                        .thenReturn(new User());
//        LoginModel goodLogin = new LoginModel("Mack", "Jones");
//        Assertions.assertTrue(userService.validateLogin(goodLogin));
//        LoginModel badLogin = new LoginModel("John", "Bones");
//        Assertions.assertFalse(userService.validateLogin(badLogin));
//    }
}
