package com.kieran.wordle.dto;

import com.kieran.wordle.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private int points;
    private int currentWordlesOwned;

    public static UserResponseDto mapUserToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .points(user.getPoints())
                .currentWordlesOwned(user.getCurrentWordlesOwned())
                .build();
    }

}
