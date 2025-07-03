package com.thelastcodebenders.epsy_backend.exceptions;

public class EntityNotFoundException extends EpsyException {
    public EntityNotFoundException(String entity) {
        super("Entity not found: " + entity);
    }
}
