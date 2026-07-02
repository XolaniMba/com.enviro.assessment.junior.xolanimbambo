package com.enviro.assessment.junior.xolanimbambo.exception;

/** Thrown when an investor looked up by id does not exist. */
public class InvestorNotFoundException extends RuntimeException {

    public InvestorNotFoundException(String message) {
        super(message);
    }
}