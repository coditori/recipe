package com.example.recipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage apiException(ApiException ex) {
        return ErrorMessage.builder()
                .exceptionType(ErrorMessage.ExceptionTypeEnum.API_EXCEPTION)
                .message(ex.getMessage())
                .time(new Date())
                .build();
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundException(ApiException ex) {
        return ErrorMessage.builder()
                .exceptionType(ErrorMessage.ExceptionTypeEnum.NOT_FOUND_EXCEPTION)
                .message(ex.getMessage())
                .time(new Date())
                .build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage unKnownException(Exception ex) {
        return ErrorMessage.builder()
                .exceptionType(ErrorMessage.ExceptionTypeEnum.EXCEPTION)
                .message("Unexpected Error occurred: " + ex.getMessage())
                .time(new Date())
                .build();
    }
}