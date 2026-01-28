package com.quality.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.stereotype.Component;

/**
 * Customizes OpenAPI documentation to enrich error responses.
 * Adds structured error code information to API responses.
 * Follows Single Responsibility Principle - only enhances OpenAPI documentation.
 * Implements Open/Closed Principle - extends functionality without modifying existing code.
 */
@Component
public class ErrorCodeOpenApiCustomizer implements OpenApiCustomizer {

    @Override
    public void customise(OpenAPI openApi) {
        if (openApi.getPaths() != null) {
            openApi.getPaths().forEach((path, pathItem) -> {
                enrichPathOperations(pathItem);
            });
        }
    }

    /**
     * Enriches all operations in a path with error code documentation.
     */
    private void enrichPathOperations(PathItem pathItem) {
        enrichOperation(pathItem.getGet());
        enrichOperation(pathItem.getPost());
        enrichOperation(pathItem.getPut());
        enrichOperation(pathItem.getDelete());
        enrichOperation(pathItem.getPatch());
    }

    /**
     * Enriches a single operation with detailed error information.
     * Adds metadata to error responses for better documentation.
     */
    private void enrichOperation(Operation operation) {
        if (operation == null || operation.getResponses() == null) {
            return;
        }

        operation.getResponses().forEach((statusCode, response) -> {
            if (isErrorResponse(statusCode)) {
                enrichErrorResponse(statusCode, response);
            }
        });
    }

    /**
     * Determines if a status code represents an error response.
     */
    private boolean isErrorResponse(String statusCode) {
        try {
            int code = Integer.parseInt(statusCode);
            return code >= 400 && code < 600;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Enriches an error response with additional metadata.
     * Ensures error responses have proper content structure.
     */
    private void enrichErrorResponse(String statusCode, ApiResponse response) {
        // Ensure response has content
        if (response.getContent() == null) {
            response.setContent(new Content());
        }

        Content content = response.getContent();
        
        // Ensure application/json media type exists
        if (!content.containsKey("application/json")) {
            MediaType mediaType = new MediaType();
            mediaType.schema(new io.swagger.v3.oas.models.media.Schema<>()
                    .type("object")
                    .$ref("#/components/schemas/ErrorResponse"));
            content.addMediaType("application/json", mediaType);
        }

        // Add extension to mark this as an error response with structured codes
        response.addExtension("x-error-structured", true);
        response.addExtension("x-error-format", "Includes typeCode, subtypeCode, and detailed error information");
    }
}
