package com.quality.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quality.exception.header.HeaderException;
import com.quality.exception.response.ErrorDetail;
import com.quality.exception.response.ErrorDetailsInfo;
import com.quality.exception.response.ErrorLink;
import com.quality.exception.response.ErrorResponse;
import com.quality.validation.HeaderConstants;
import com.quality.validation.HeaderValidator;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Filter to validate required HTTP headers on all requests.
 * Applies to all endpoints and HTTP methods automatically (GET, POST, PUT, DELETE, etc.).
 * Follows Single Responsibility Principle (SRP) - only validates headers.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class HeaderValidationFilter implements Filter {

    private final ObjectMapper objectMapper;

    public HeaderValidationFilter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String requestPath = request.getRequestURI();

        // Skip header validation for Swagger/OpenAPI documentation paths
        if (isSwaggerPath(requestPath)) {
            chain.doFilter(req, res);
            return;
        }

        try {
            // Validate all required headers for every request
            for (String headerName : HeaderConstants.REQUIRED_HEADERS) {
                String value = request.getHeader(headerName);
                // Validation logic is delegated to HeaderValidator
                // If validation fails, it throws a HeaderException
                HeaderValidator.validate(headerName, value);
            }

            // Continue the filter chain if all validations pass
            chain.doFilter(req, res);
        } catch (HeaderException ex) {
            // Handle header validation errors and return structured JSON response
            buildErrorResponse(ex, request, response);
        }
    }

    /**
     * Check if the request path is for Swagger/OpenAPI documentation.
     * These paths should not require custom headers.
     */
    private boolean isSwaggerPath(String path) {
        return path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/swagger-resources") ||
               path.startsWith("/webjars/");
    }

    /**
     * Builds and writes the error response as JSON.
     */
    private void buildErrorResponse(HeaderException ex, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Build HATEOAS links
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

        ErrorResponse errorResponse = new ErrorResponse(detail);

        // Set response properties
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // Write JSON response
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }
}
