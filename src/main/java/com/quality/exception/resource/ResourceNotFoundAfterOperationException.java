package com.quality.exception.resource;

import com.quality.exception.response.ErrorLink;
import com.quality.validation.ErrorCodeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception thrown when a resource is not found after an update or delete operation.
 * Used for PUT, DELETE operations when the resource doesn't exist.
 * Code: RNF-002
 */
public class ResourceNotFoundAfterOperationException extends ResourceNotFoundException {

    private final String operation;

    public ResourceNotFoundAfterOperationException(String resourceType, Object id, String operation) {
        super(
                "Cannot " + operation + " " + resourceType + " with ID " + id + ": resource not found",
                resourceType,
                "id",
                id.toString(),
                ErrorCodeConstants.TYPE_CODE_RESOURCE,
                ErrorCodeConstants.TYPE_RESOURCE,
                ErrorCodeConstants.SUBTYPE_CODE_NOT_FOUND_AFTER_OPERATION,
                ErrorCodeConstants.SUBTYPE_NOT_FOUND_AFTER_OPERATION
        );
        this.operation = operation;
    }

    @Override
    public String getSuggestion() {
        return "The resource may have been deleted or the ID is incorrect. " +
                "Verify the resource exists before attempting to " + operation + " it.";
    }

    @Override
    public String getDocumentationUrl() {
        return ErrorCodeConstants.DOCUMENTATION_BASE_URL + ErrorCodeConstants.SUBTYPE_CODE_NOT_FOUND_AFTER_OPERATION;
    }

    @Override
    public Map<String, ErrorLink> getAdditionalLinks(String basePath) {
        Map<String, ErrorLink> links = new HashMap<>();
        links.put("collection", ErrorLink.builder()
                .href(basePath)
                .method("GET")
                .build());
        links.put("create", ErrorLink.builder()
                .href(basePath)
                .method("POST")
                .build());
        return links;
    }
}
