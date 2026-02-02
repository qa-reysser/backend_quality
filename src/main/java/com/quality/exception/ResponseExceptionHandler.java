package com.quality.exception;

import com.quality.exception.header.HeaderException;
import com.quality.exception.resource.ResourceNotFoundException;
import com.quality.exception.response.*;
import com.quality.exception.validation.DuplicateFieldException;
import com.quality.validation.ErrorCodeConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Generic handler for all HeaderException types.
     * Applies Open/Closed Principle (OCP) - handles all header exceptions polymorphically.
     */
    @ExceptionHandler(HeaderException.class)
    @NonNull
    public ResponseEntity<com.quality.exception.response.ErrorResponse> handleHeaderException(
            @NonNull HeaderException ex,
            @NonNull HttpServletRequest request
    ) {
        com.quality.exception.response.ErrorResponse response = ErrorResponseBuilder.build(ex, request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Generic handler for all ResourceNotFoundException types (404 errors).
     * Applies Open/Closed Principle (OCP) - handles all resource not found exceptions polymorphically.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @NonNull
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            @NonNull ResourceNotFoundException ex,
            @NonNull HttpServletRequest request
    ) {
        ErrorResponse response = ResourceErrorResponseBuilder.build(ex, request);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handler for DuplicateFieldException (RBV-005).
     * Handles duplicate value errors with structured validation error response.
     * Returns HTTP 409 CONFLICT.
     */
    @ExceptionHandler(DuplicateFieldException.class)
    @NonNull
    public ResponseEntity<ErrorResponse> handleDuplicateFieldException(
            @NonNull DuplicateFieldException ex,
            @NonNull HttpServletRequest request
    ) {
        String path = Objects.requireNonNull(request.getRequestURI(), "Request URI cannot be null");
        String method = Objects.requireNonNull(request.getMethod(), "Request method cannot be null");

        // Build HATEOAS links
        Map<String, ErrorLink> links = new HashMap<>();
        links.put("self", ErrorLink.builder().href(path).method(method).build());
        links.put("documentation", ErrorLink.builder().href(ErrorCodeConstants.DOCUMENTATION_BASE_URL + ex.getSubtypeCode()).build());

        // Build error details info
        ErrorDetailsValidationInfo detailsInfo = new ErrorDetailsValidationInfo(
                ex.getFieldName(),
                ex.getFieldValue(),
                ex.getConstraint()
        );

        // Build complete error detail (same structure as headers and other validations)
        ErrorDetailValidation errorDetail = ErrorDetailValidation.builder()
                .timestamp(LocalDateTime.now())
                .status(409)
                .error("Conflict")
                .message(String.format("Duplicate value '%s' detected for field '%s'", ex.getFieldValue(), ex.getFieldName()))
                .typeCode(ex.getTypeCode())
                .type(ErrorCodeConstants.TYPE_VALIDATION)
                .subtypeCode(ex.getSubtypeCode())
                .subtype(ErrorCodeConstants.SUBTYPE_DUPLICATE_VALUE)
                .details(detailsInfo)
                .path(path)
                .documentationUrl(ErrorCodeConstants.DOCUMENTATION_BASE_URL + ex.getSubtypeCode())
                ._links(links)
                .build();

        ErrorResponse response = new ErrorResponse(errorDetail);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    @NonNull
    public ResponseEntity<CustomErrorResponse> handleAllException(@NonNull ModelNotFoundException ex, @NonNull WebRequest request) {
        CustomErrorResponse err = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    @NonNull
    public org.springframework.web.ErrorResponse handleModelNotFoundException(@NonNull ModelNotFoundException ex, @NonNull WebRequest req) {
        return org.springframework.web.ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, Objects.requireNonNull(ex.getMessage(), "Error message cannot be null"))
                .title("Model not found")
                .type(Objects.requireNonNull(URI.create(req.getContextPath())))
                .property("test", "value-test")
                .property("age", 32)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    @NonNull
    public ResponseEntity<CustomErrorResponse> handleSQLException(@NonNull SQLException ex, @NonNull WebRequest req) {
        CustomErrorResponse res = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), req.getDescription(false));
        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }


    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        // Process first field error to get details (similar to header error structure)
        FieldError firstError = ex.getBindingResult().getFieldErrors().get(0);
        String fieldName = firstError.getField();
        Object invalidValue = firstError.getRejectedValue();
        String errorCode = firstError.getCode();
        
        String subTypeCode;
        String subTypeDescription;
        String constraint;
        String message;

        // Map validation annotation to error codes
        switch (errorCode != null ? errorCode : "") {
            case "NotNull":
                subTypeCode = ErrorCodeConstants.SUBTYPE_CODE_FIELD_REQUIRED;
                subTypeDescription = ErrorCodeConstants.SUBTYPE_FIELD_REQUIRED;
                constraint = "Field is required and cannot be null";
                message = String.format("Field '%s' is required and cannot be null", fieldName);
                break;
            case "NotEmpty":
                subTypeCode = ErrorCodeConstants.SUBTYPE_CODE_FIELD_EMPTY;
                subTypeDescription = ErrorCodeConstants.SUBTYPE_FIELD_EMPTY;
                constraint = "Field cannot be empty";
                message = String.format("Field '%s' cannot be empty", fieldName);
                break;
            case "Size":
                // Analyze the actual constraint violations to determine min or max
                Object[] arguments = firstError.getArguments();
                if (arguments != null && arguments.length >= 3) {
                    // Spring Boot argument order: [0] = constraint annotation, [1] = max, [2] = min
                    Integer maxValue = (Integer) arguments[1];
                    Integer minValue = (Integer) arguments[2];
                    String value = invalidValue != null ? invalidValue.toString() : "";
                    int actualLength = value.length();
                    
                    if (actualLength < minValue) {
                        // Too short
                        subTypeCode = ErrorCodeConstants.SUBTYPE_CODE_LENGTH_TOO_SHORT;
                        subTypeDescription = ErrorCodeConstants.SUBTYPE_LENGTH_TOO_SHORT;
                        constraint = String.format("Minimum length is %d characters", minValue);
                        message = String.format("Field '%s' length is below minimum (%d characters required)", fieldName, minValue);
                    } else {
                        // Too long
                        subTypeCode = ErrorCodeConstants.SUBTYPE_CODE_LENGTH_TOO_LONG;
                        subTypeDescription = ErrorCodeConstants.SUBTYPE_LENGTH_TOO_LONG;
                        constraint = String.format("Maximum length is %d characters", maxValue);
                        message = String.format("Field '%s' length exceeds maximum (%d characters allowed)", fieldName, maxValue);
                    }
                } else {
                    // Fallback
                    subTypeCode = "RBV-003";
                    subTypeDescription = "field_length_invalid";
                    constraint = Objects.requireNonNullElse(firstError.getDefaultMessage(), "Invalid length");
                    message = Objects.requireNonNullElse(firstError.getDefaultMessage(), "Invalid length");
                }
                break;
            default:
                // Generic validation error
                subTypeCode = "RBV-000";
                subTypeDescription = "validation_error";
                constraint = Objects.requireNonNullElse(firstError.getDefaultMessage(), "Validation error");
                message = Objects.requireNonNullElse(firstError.getDefaultMessage(), "Validation error");
                break;
        }

        // Build error detail in same structure as headers (single object, not array)
        String requestUri = Objects.requireNonNull(
            ((org.springframework.web.context.request.ServletWebRequest) request).getRequest().getRequestURI(),
            "Request URI cannot be null"
        );
        String requestMethod = Objects.requireNonNull(
            ((org.springframework.web.context.request.ServletWebRequest) request).getRequest().getMethod(),
            "Request method cannot be null"
        );

        Map<String, ErrorLink> links = new HashMap<>();
        links.put("self", ErrorLink.builder().href(requestUri).method(requestMethod).build());
        links.put("documentation", ErrorLink.builder().href(ErrorCodeConstants.DOCUMENTATION_BASE_URL + subTypeCode).build());

        // Build error details info
        ErrorDetailsValidationInfo detailsInfo = new ErrorDetailsValidationInfo(
                fieldName,
                invalidValue,
                constraint
        );

        // Build complete error detail (same structure as header errors)
        ErrorDetailValidation errorDetail = ErrorDetailValidation.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message(message)
                .typeCode(ErrorCodeConstants.TYPE_CODE_VALIDATION)
                .type(ErrorCodeConstants.TYPE_VALIDATION)
                .subtypeCode(subTypeCode)
                .subtype(subTypeDescription)
                .details(detailsInfo)
                .path(requestUri)
                .documentationUrl(ErrorCodeConstants.DOCUMENTATION_BASE_URL + subTypeCode)
                ._links(links)
                .build();

        ErrorResponse response = new ErrorResponse(errorDetail);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handler for NoHandlerFoundException (404 - endpoint not found).
     * Triggered when a requested URL path doesn't match any controller mapping.
     */
    @Override
    @NonNull
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @NonNull org.springframework.web.servlet.NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        String requestUri = Objects.requireNonNull(
            ((org.springframework.web.context.request.ServletWebRequest) request).getRequest().getRequestURI(),
            "Request URI cannot be null"
        );
        String requestMethod = Objects.requireNonNull(
            ((org.springframework.web.context.request.ServletWebRequest) request).getRequest().getMethod(),
            "Request method cannot be null"
        );

        // Build HATEOAS links
        Map<String, ErrorLink> links = new HashMap<>();
        links.put("self", ErrorLink.builder().href(requestUri).method(requestMethod).build());
        links.put("documentation", ErrorLink.builder()
                .href(ErrorCodeConstants.DOCUMENTATION_BASE_URL + ErrorCodeConstants.SUBTYPE_CODE_ENDPOINT_NOT_FOUND)
                .build());
        links.put("api-root", ErrorLink.builder().href("/").build());

        // Build error details for endpoint not found
        ErrorDetailsResourceInfo detailsInfo = ErrorDetailsResourceInfo.builder()
                .resourceType("Endpoint")
                .searchCriteria("URL path")
                .searchValue(requestUri)
                .suggestion("Verify the URL path is correct. Check available endpoints in the API documentation.")
                .build();

        // Build complete error detail (same structure as other errors)
        ErrorDetailResource errorDetail = ErrorDetailResource.builder()
                .timestamp(LocalDateTime.now())
                .status(404)
                .error("Not Found")
                .message(String.format("No endpoint found for %s %s", requestMethod, requestUri))
                .typeCode(ErrorCodeConstants.TYPE_CODE_RESOURCE)
                .type(ErrorCodeConstants.TYPE_RESOURCE)
                .subtypeCode(ErrorCodeConstants.SUBTYPE_CODE_ENDPOINT_NOT_FOUND)
                .subtype(ErrorCodeConstants.SUBTYPE_ENDPOINT_NOT_FOUND)
                .details(detailsInfo)
                .path(requestUri)
                .documentationUrl(ErrorCodeConstants.DOCUMENTATION_BASE_URL + ErrorCodeConstants.SUBTYPE_CODE_ENDPOINT_NOT_FOUND)
                .links(links)
                .build();

        ErrorResponse response = new ErrorResponse(errorDetail);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
