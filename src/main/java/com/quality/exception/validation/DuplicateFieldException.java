package com.quality.exception.validation;

import com.quality.validation.ErrorCodeConstants;
import lombok.Getter;

/**
 * Exception thrown when a duplicate value is detected for a unique field.
 * This is a runtime exception for business rule violations.
 */
@Getter
public class DuplicateFieldException extends RuntimeException {
    
    private final String fieldName;
    private final Object fieldValue;
    private final String typeCode = ErrorCodeConstants.TYPE_CODE_VALIDATION;
    private final String subtypeCode = ErrorCodeConstants.SUBTYPE_CODE_DUPLICATE_VALUE;

    /**
     * Constructor for DuplicateFieldException.
     * 
     * @param fieldName the name of the field that has a duplicate value
     * @param fieldValue the duplicate value
     */
    public DuplicateFieldException(String fieldName, Object fieldValue) {
        super(String.format("Duplicate value '%s' detected for field '%s'", fieldValue, fieldName));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Get the constraint message.
     * 
     * @return the constraint violation message
     */
    public String getConstraint() {
        return "Value already exists in database";
    }
}
