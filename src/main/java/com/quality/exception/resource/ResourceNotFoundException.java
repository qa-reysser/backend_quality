package com.quality.exception.resource;

import com.quality.exception.response.ErrorLink;
import lombok.Getter;

import java.util.Map;

/**
 * Base abstract class for all resource not found exceptions (404 errors).
 * Follows the Open/Closed Principle (OCP) - open for extension, closed for modification.
 */
@Getter
public abstract class ResourceNotFoundException extends RuntimeException {

    private final String resourceType;
    private final String searchCriteria;
    private final String searchValue;
    private final String typeCode;
    private final String type;
    private final String subtypeCode;
    private final String subtype;

    protected ResourceNotFoundException(
            String message,
            String resourceType,
            String searchCriteria,
            String searchValue,
            String typeCode,
            String type,
            String subtypeCode,
            String subtype
    ) {
        super(message);
        this.resourceType = resourceType;
        this.searchCriteria = searchCriteria;
        this.searchValue = searchValue;
        this.typeCode = typeCode;
        this.type = type;
        this.subtypeCode = subtypeCode;
        this.subtype = subtype;
    }

    /**
     * Returns a suggestion message to help the user resolve the error.
     */
    public abstract String getSuggestion();

    /**
     * Returns the documentation URL for this specific error.
     */
    public abstract String getDocumentationUrl();

    /**
     * Returns additional HATEOAS links for navigation.
     * @param basePath the base path for the resource (e.g., "/priorities")
     */
    public abstract Map<String, ErrorLink> getAdditionalLinks(String basePath);
}
