package com.kieran.wordle.advice;

import com.kieran.wordle.exception.BadRequestException;
import com.kieran.wordle.exception.NotAuthorizedException;
import com.kieran.wordle.exception.NotFoundException;
import com.kieran.wordle.model.ErrorResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice(basePackages = {"com.kieran.wordle"})
@ResponseBody
public class ControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseModel handleBadRequestException(BadRequestException e) {
        return new ErrorResponseModel(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseModel handleNotFoundException(NotFoundException e) {
        return new ErrorResponseModel(e.getMessage());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseModel handleNotAuthorizedException(NotAuthorizedException e) {
        return new ErrorResponseModel(e.getMessage());
    }
}
