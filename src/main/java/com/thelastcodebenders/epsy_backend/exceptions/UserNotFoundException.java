package com.thelastcodebenders.epsy_backend.exceptions;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("USER");
    }
}
