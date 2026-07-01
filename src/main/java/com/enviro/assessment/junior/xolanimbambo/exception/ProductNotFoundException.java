package com.enviro.assessment.junior.xolanimbambo.exception;

/** Thrown when an investment product looked up by id does not exist. */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}