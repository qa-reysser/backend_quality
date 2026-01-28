package com.quality.exception.response;

import com.quality.exception.header.HeaderException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Builder for creating structured error responses dynamically.
 * Applies Single Responsibility Principle (SRP) - only builds error responses.
 */
public class ErrorResponseBuilder {

    private ErrorResponseBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Builds a complete ErrorResponse from a HeaderException and HttpServletRequest.
     * Extracts dynamic information from the request (path, method, etc.).
     *
     * @param ex the header exception
     * @param request the HTTP request
     * @return a fully constructed ErrorResponse
     */
    public static ErrorResponse build(HeaderException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Build HATEOAS links dynamically
        Map<String, ErrorLink> links = Map.of(
                "self", ErrorLink.builder()
                        .href(path)
                        .method(method)
                        .build(),
                "documentation", ErrorLink.builder()
                        .href(ex.getDocumentationUrl())
                        .build()
        );

        // Build error details info
        ErrorDetailsInfo detailsInfo = ErrorDetailsInfo.builder()
                .problematicField(ex.getHeaderName())
                .invalidValue(ex.getInvalidValue())
                .correctFormat(ex.getCorrectFormat())
                .build();

        // Build complete error detail
        ErrorDetail detail = ErrorDetail.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
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
}
