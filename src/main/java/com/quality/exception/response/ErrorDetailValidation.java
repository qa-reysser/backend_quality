package com.quality.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for validation errors.
 * Same structure as ErrorDetail to maintain consistency across all error types.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetailValidation {
    /**
     * Timestamp when the error occurred.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * HTTP status code.
     */
    private Integer status;

    /**
     * HTTP status description.
     */
    private String error;

    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * The general error type code (TYP-003).
     */
    private String typeCode;

    /**
     * The general error type description.
     */
    private String type;

    /**
     * The specific error subtype code.
     * Example: "RBV-001", "RBV-002", "RBV-003", "RBV-004", "RBV-005"
     */
    private String subtypeCode;

    /**
     * Human-readable description of the error subtype.
     */
    private String subtype;

    /**
     * Detailed information about the validation error.
     */
    private ErrorDetailsValidationInfo details;

    /**
     * Request path where the error occurred.
     */
    private String path;

    /**
     * URL to documentation about this error.
     */
    private String documentationUrl;

    /**
     * HATEOAS links for navigation.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, ErrorLink> _links;
}
