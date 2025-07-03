package com.thelastcodebenders.epsy_backend.exceptions;

public class ProductNotFoundException extends EntityNotFoundException {
    public ProductNotFoundException() {
        super("PRODUCT");
    }
}
