package com.quality.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Wrapper for error responses.
 * This is the root object returned in case of errors.
 * Generic to support header errors, resource errors, and validation errors.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Estructura de respuesta de error")
public class ErrorResponse {
    /**
     * The type code of the error (TYP-001, TYP-002, TYP-003).
     * Only used for validation errors (TYP-003).
     */
    @Schema(description = "Código del tipo de error", example = "TYP-001")
    private String typeCode;

    /**
     * The type description of the error.
     * Only used for validation errors (TYP-003).
     */
    @Schema(description = "Descripción del tipo de error", example = "header_error")
    private String typeDescription;

    /**
     * Timestamp when the error occurred.
     * Only used for validation errors (TYP-003).
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Fecha y hora del error", example = "2025-11-13T10:30:00")
    private LocalDateTime timestamp;

    /**
     * List of detailed errors.
     * Can be ErrorDetail (headers), ErrorDetailResource (404), or List<ErrorDetailValidation> (validation).
     */
    @Schema(description = "Detalle completo del error", implementation = ErrorDetail.class)
    private Object errors;

    /**
     * HATEOAS links for navigation.
     * Only used for validation errors (TYP-003).
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Map<String, String>> _links;

    /**
     * Constructor for header and resource errors (backward compatibility).
     * @param errors ErrorDetail or ErrorDetailResource
     */
    public ErrorResponse(Object errors) {
        this.errors = errors;
    }

    /**
     * Constructor for validation errors (new structured format).
     * @param typeCode TYP-003
     * @param typeDescription "request_body_validation_error"
     * @param timestamp when error occurred
     * @param errors List<ErrorDetailValidation>
     * @param links HATEOAS links
     */
    public ErrorResponse(String typeCode, String typeDescription, LocalDateTime timestamp, 
                        Object errors, Map<String, Map<String, String>> links) {
        this.typeCode = typeCode;
        this.typeDescription = typeDescription;
        this.timestamp = timestamp;
        this.errors = errors;
        this._links = links;
    }
}
