package com.quality.validation;

import com.quality.exception.header.*;

import java.util.regex.Pattern;

/**
 * Validator for HTTP headers.
 * Applies Single Responsibility Principle (SRP) - only validates headers.
 */
public class HeaderValidator {

    private static final Pattern UUID_PATTERN = Pattern.compile(HeaderConstants.UUID_PATTERN);

    private HeaderValidator() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates a header value according to UUID format rules.
     * Throws specific exceptions for different validation failures.
     *
     * @param headerName the name of the header
     * @param value the value to validate
     * @throws HeaderException if validation fails
     */
    public static void validate(String headerName, String value) {
        validatePresence(headerName, value);
        validateLength(headerName, value);
        validateUUIDFormat(headerName, value);
    }

    /**
     * Validates that the header is present and not blank.
     */
    private static void validatePresence(String headerName, String value) {
        if (value == null || value.isBlank()) {
            throw new MissingHeaderException(headerName, "Header value is missing or null");
        }
    }

    /**
     * Validates that the header has the correct length (36 characters for UUID).
     */
    private static void validateLength(String headerName, String value) {
        int length = value.length();
        
        if (length < HeaderConstants.UUID_LENGTH) {
            throw new HeaderTooShortException(headerName, value);
        }
        
        if (length > HeaderConstants.UUID_LENGTH) {
            throw new HeaderTooLongException(headerName, value);
        }
    }

    /**
     * Validates that the header complies with UUID format.
     * This includes checking for valid hexadecimal characters, correct structure,
     * and proper hyphen placement.
     */
    private static void validateUUIDFormat(String headerName, String value) {
        if (!UUID_PATTERN.matcher(value).matches()) {
            throw new InvalidHeaderFormatException(headerName, value);
        }
    }
}
