package com.quality.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for detailed validation error information.
 * Contains information about which field failed validation and why.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetailsValidationInfo {
    /**
     * The field name that failed validation.
     * Example: "name", "description"
     */
    private String problematicField;

    /**
     * The invalid value that was provided.
     * Example: null, "", "AB", "duplicated value"
     */
    private Object invalidValue;

    /**
     * The constraint that was violated.
     * Example: "Field is required and cannot be null",
     *          "Minimum length is 3 characters",
     *          "Maximum length is 70 characters",
     *          "Value already exists in database"
     */
    private String constraint;
}
