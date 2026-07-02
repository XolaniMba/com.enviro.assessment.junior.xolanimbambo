package com.enviro.assessment.junior.xolanimbambo.exception;

/**
 * Thrown when a withdrawal request breaks a business rule, e.g. exceeds the
 * available balance, exceeds the 90% cap, or the retirement age restriction
 * is not met.
 */
public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}