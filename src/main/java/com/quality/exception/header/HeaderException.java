package com.quality.exception.header;

import lombok.Getter;

/**
 * Base abstract class for all header-related exceptions.
 * Follows the Open/Closed Principle (OCP) - open for extension, closed for modification.
 */
@Getter
public abstract class HeaderException extends RuntimeException {

    private final String headerName;
    private final String invalidValue;
    private final String typeCode;
    private final String type;
    private final String subtypeCode;
    private final String subtype;

    protected HeaderException(
            String message,
            String headerName,
            String invalidValue,
            String typeCode,
            String type,
            String subtypeCode,
            String subtype
    ) {
        super(message);
        this.headerName = headerName;
        this.invalidValue = invalidValue;
        this.typeCode = typeCode;
        this.type = type;
        this.subtypeCode = subtypeCode;
        this.subtype = subtype;
    }

    /**
     * Returns the correct format message for this specific error.
     */
    public abstract String getCorrectFormat();

    /**
     * Returns the documentation URL for this specific error.
     */
    public abstract String getDocumentationUrl();
}
