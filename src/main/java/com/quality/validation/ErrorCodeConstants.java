package com.quality.validation;

/**
 * Constants for error codes and types.
 * Centralizes all error classification codes.
 */
public class ErrorCodeConstants {

    // ========== Type codes (general categories) ==========
    
    // Header errors
    public static final String TYPE_CODE_HEADER = "TYP-001";
    public static final String TYPE_HEADER = "header_error";

    // Resource not found errors
    public static final String TYPE_CODE_RESOURCE = "TYP-002";
    public static final String TYPE_RESOURCE = "resource_not_found";

    // Request body validation errors
    public static final String TYPE_CODE_VALIDATION = "TYP-003";
    public static final String TYPE_VALIDATION = "request_body_validation_error";

    // ========== Header error subtypes (TYP-001) ==========
    
    public static final String SUBTYPE_CODE_MISSING = "HDR-001";
    public static final String SUBTYPE_MISSING = "missing_header";

    public static final String SUBTYPE_CODE_TOO_SHORT = "HDR-002";
    public static final String SUBTYPE_TOO_SHORT = "header_length_too_short";

    public static final String SUBTYPE_CODE_TOO_LONG = "HDR-003";
    public static final String SUBTYPE_TOO_LONG = "header_length_too_long";

    public static final String SUBTYPE_CODE_INVALID_FORMAT = "HDR-004";
    public static final String SUBTYPE_INVALID_FORMAT = "invalid_header_format";

    // ========== Resource not found subtypes (TYP-002) ==========
    
    public static final String SUBTYPE_CODE_NOT_FOUND_BY_ID = "RNF-001";
    public static final String SUBTYPE_NOT_FOUND_BY_ID = "resource_not_found_by_id";

    public static final String SUBTYPE_CODE_NOT_FOUND_AFTER_OPERATION = "RNF-002";
    public static final String SUBTYPE_NOT_FOUND_AFTER_OPERATION = "resource_not_found_after_operation";

    public static final String SUBTYPE_CODE_ENDPOINT_NOT_FOUND = "RNF-003";
    public static final String SUBTYPE_ENDPOINT_NOT_FOUND = "endpoint_not_found";

    // ========== Request body validation subtypes (TYP-003) ==========
    
    public static final String SUBTYPE_CODE_FIELD_REQUIRED = "RBV-001";
    public static final String SUBTYPE_FIELD_REQUIRED = "required_field_missing";

    public static final String SUBTYPE_CODE_FIELD_EMPTY = "RBV-002";
    public static final String SUBTYPE_FIELD_EMPTY = "field_value_empty";

    public static final String SUBTYPE_CODE_LENGTH_TOO_SHORT = "RBV-003";
    public static final String SUBTYPE_LENGTH_TOO_SHORT = "field_length_below_minimum";

    public static final String SUBTYPE_CODE_LENGTH_TOO_LONG = "RBV-004";
    public static final String SUBTYPE_LENGTH_TOO_LONG = "field_length_exceeds_maximum";

    public static final String SUBTYPE_CODE_DUPLICATE_VALUE = "RBV-005";
    public static final String SUBTYPE_DUPLICATE_VALUE = "duplicate_value_detected";

    // ========== Documentation URLs ==========
    
    // Documentation base URL (API Documentation)
    public static final String DOCUMENTATION_BASE_URL = "http://localhost:8080/api/docs#/";

    private ErrorCodeConstants() {
        // Private constructor to prevent instantiation
    }
}
