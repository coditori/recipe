package com.example.recipe.exception;

public class EntityNotFoundException extends ApiException {
    public EntityNotFoundException(String message)
    {
        super(message);
    }
}
