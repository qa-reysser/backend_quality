package com.quality.exception.response;

import com.quality.exception.resource.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Builder for creating structured error responses for resource not found errors (404).
 * Applies Single Responsibility Principle (SRP) - only builds resource error responses.
 */
public class ResourceErrorResponseBuilder {

    private ResourceErrorResponseBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Builds a complete ErrorResponse from a ResourceNotFoundException and HttpServletRequest.
     * Extracts dynamic information from the request (path, method, etc.).
     *
     * @param ex the resource not found exception
     * @param request the HTTP request
     * @return a fully constructed ErrorResponse
     */
    public static ErrorResponse build(ResourceNotFoundException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Extract base path for the resource (e.g., "/priorities/5" -> "/priorities")
        String basePath = extractBasePath(path);

        // Build HATEOAS links
        Map<String, ErrorLink> links = new HashMap<>();
        
        // Self link
        links.put("self", ErrorLink.builder()
                .href(path)
                .method(method)
                .build());
        
        // Add additional links from the exception (collection, create, etc.)
        links.putAll(ex.getAdditionalLinks(basePath));
        
        // Documentation link
        links.put("documentation", ErrorLink.builder()
                .href(ex.getDocumentationUrl())
                .build());

        // Build error details info for resources
        ErrorDetailsResourceInfo detailsInfo = ErrorDetailsResourceInfo.builder()
                .resourceType(ex.getResourceType())
                .searchCriteria(ex.getSearchCriteria())
                .searchValue(ex.getSearchValue())
                .suggestion(ex.getSuggestion())
                .build();

        // Build complete error detail
        ErrorDetailResource detail = ErrorDetailResource.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Not Found")
                .message(ex.getMessage())
                .typeCode(ex.getTypeCode())
                .type(ex.getType())
                .subtypeCode(ex.getSubtypeCode())
                .subtype(ex.getSubtype())
                .details(detailsInfo)
                .path(path)
                .documentationUrl(ex.getDocumentationUrl())
                .links(links)
                .build();

        return new ErrorResponse(detail);
    }

    /**
     * Extracts the base path from a full path.
     * Example: "/priorities/5" -> "/priorities"
     */
    private static String extractBasePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        
        // Remove trailing slash
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        
        // Find last segment
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash > 0) {
            String lastSegment = path.substring(lastSlash + 1);
            // If last segment is a number (ID), remove it
            if (lastSegment.matches("\\d+")) {
                return path.substring(0, lastSlash);
            }
        }
        
        return path;
    }
}
