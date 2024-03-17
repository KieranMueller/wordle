package com.kieran.wordle.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BadRequestException extends RuntimeException {
    private String message;
}
