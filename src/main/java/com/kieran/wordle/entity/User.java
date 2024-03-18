package com.kieran.wordle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int points;
    private int currentWordlesOwned;
    @Builder.Default
    private String emailUuid = String.valueOf(UUID.randomUUID());
    private boolean confirmedEmail;
    @Builder.Default
    private String resetPasswordUuid = String.valueOf(UUID.randomUUID());
    private String newPassword;

}
