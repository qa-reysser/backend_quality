package com.quality.validation;

/**
 * Constants for HTTP headers validation.
 * Centralizes all header names and validation parameters.
 */
public class HeaderConstants {

    // Header names
    public static final String X_CORRELATION_ID = "x-correlation-id";
    public static final String X_REQUEST_ID = "x-request-id";
    public static final String X_TRANSACTION_ID = "x-transaction-id";

    // Required headers array
    public static final String[] REQUIRED_HEADERS = {
            X_CORRELATION_ID,
            X_REQUEST_ID,
            X_TRANSACTION_ID
    };

    // Validation parameters
    public static final int UUID_LENGTH = 36;
    public static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    // Error messages
    public static final String CORRECT_FORMAT_MESSAGE = "The value should be a valid UUID with exactly 36 characters.";

    private HeaderConstants() {
        // Private constructor to prevent instantiation
    }
}
