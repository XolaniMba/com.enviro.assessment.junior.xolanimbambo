package com.enviro.assessment.junior.xolanimbambo.exception;

/** Thrown when an investor's age fails a validation rule (e.g. retirement age restriction). */
public class InvalidAgeException extends RuntimeException {

    public InvalidAgeException(String message) {
        super(message);
    }
}