package com.quality.exception.resource;

import com.quality.exception.response.ErrorLink;
import com.quality.validation.ErrorCodeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception thrown when a resource is not found by its ID.
 * Used for GET, PUT, DELETE operations when the specified ID doesn't exist.
 * Code: RNF-001
 */
public class ResourceNotFoundByIdException extends ResourceNotFoundException {

    public ResourceNotFoundByIdException(String resourceType, Object id) {
        super(
                resourceType + " with ID " + id + " not found",
                resourceType,
                "id",
                id.toString(),
                ErrorCodeConstants.TYPE_CODE_RESOURCE,
                ErrorCodeConstants.TYPE_RESOURCE,
                ErrorCodeConstants.SUBTYPE_CODE_NOT_FOUND_BY_ID,
                ErrorCodeConstants.SUBTYPE_NOT_FOUND_BY_ID
        );
    }

    @Override
    public String getSuggestion() {
        return "Verify that the ID exists or check the available resources at GET /" 
                + getResourceType().toLowerCase() + "s";
    }

    @Override
    public String getDocumentationUrl() {
        return ErrorCodeConstants.DOCUMENTATION_BASE_URL + ErrorCodeConstants.SUBTYPE_CODE_NOT_FOUND_BY_ID;
    }

    @Override
    public Map<String, ErrorLink> getAdditionalLinks(String basePath) {
        Map<String, ErrorLink> links = new HashMap<>();
        links.put("collection", ErrorLink.builder()
                .href(basePath)
                .method("GET")
                .build());
        return links;
    }
}
