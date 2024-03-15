package com.kieran.wordle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordleApplication.class, args);
	}

}
// TODO
// ensure JWT is headers of requests, wordle controller endpoints need to be protected
// Create separate endpoints and logic for updating email, password, or username
// Send email to user to confirm before they are able to login (wait for mailtrap to verify domain)
// Implement Global Exception Handler (good messages)
//Currently working on UserService.createNewUser method
// Ensure user has confirmed email before able to login
// Create valid way to create new users with valid info, bcrypt passwords, and properly login users
// Create way for existing users to update fields (new name, password, email, phone etc.)
// Ensure security, masked DB passwords on github etc, user passwords
// Create test classes for every method, check coverage