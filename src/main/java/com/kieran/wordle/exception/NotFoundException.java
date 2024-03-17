package com.kieran.wordle.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotFoundException extends RuntimeException {
    private String message;
}
