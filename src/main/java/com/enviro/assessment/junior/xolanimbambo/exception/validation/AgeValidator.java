package com.enviro.assessment.junior.xolanimbambo.exception.validation;

import com.enviro.assessment.junior.xolanimbambo.exception.InvalidAgeException;

import java.time.LocalDate;
import java.time.Period;

/**
 * Validates that an investor meets the minimum age requirement to be
 * registered on the platform. Kept as a small, focused, reusable class
 * rather than inlining this check directly in the service - easy to unit
 * test on its own and easy to reuse if age rules need to apply elsewhere.
 */
public class AgeValidator {

    private static final int MINIMUM_AGE = 18;

    private AgeValidator() {
        // Utility class - not meant to be instantiated.
    }

    public static void validate(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new InvalidAgeException("Date of birth is required.");
        }

        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidAgeException("Date of birth cannot be in the future.");
        }

        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();

        if (age < MINIMUM_AGE) {
            throw new InvalidAgeException(
                    "Investor must be at least " + MINIMUM_AGE + " years old. Current age: " + age + ".");
        }
    }
}