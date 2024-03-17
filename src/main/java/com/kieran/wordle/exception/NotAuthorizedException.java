package com.kieran.wordle.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotAuthorizedException extends RuntimeException{
    private String message;
}
