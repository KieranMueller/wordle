package com.kieran.wordle.repository;

import com.kieran.wordle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPasswordAndConfirmedEmailTrue(String username, String password);
    User findByUsernameIgnoreCase(String username);
    User findByEmailIgnoreCase(String email);
    User findByUsernameIgnoreCaseAndConfirmedEmailTrue(String username);
}
