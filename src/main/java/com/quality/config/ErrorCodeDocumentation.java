package com.quality.config;

import com.quality.validation.ErrorCodeConstants;

import java.util.*;

/**
 * Centralized documentation for error codes and subtypes.
 * Provides structured information about all possible errors in the API.
 * Follows Single Responsibility Principle - only manages error code documentation.
 * Implements Strategy pattern for flexible error documentation retrieval.
 */
public class ErrorCodeDocumentation {

    /**
     * Represents detailed information about an error code.
     */
    public static class ErrorCodeInfo {
        private final String code;
        private final String name;
        private final String description;
        private final int httpStatus;
        private final String typeCode;
        private final List<ErrorSubtypeInfo> subtypes;

        public ErrorCodeInfo(String code, String name, String description, int httpStatus, String typeCode, List<ErrorSubtypeInfo> subtypes) {
            this.code = code;
            this.name = name;
            this.description = description;
            this.httpStatus = httpStatus;
            this.typeCode = typeCode;
            this.subtypes = subtypes != null ? new ArrayList<>(subtypes) : new ArrayList<>();
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getHttpStatus() { return httpStatus; }
        public String getTypeCode() { return typeCode; }
        public List<ErrorSubtypeInfo> getSubtypes() { return new ArrayList<>(subtypes); }
    }

    /**
     * Represents detailed information about an error subtype.
     */
    public static class ErrorSubtypeInfo {
        private final String code;
        private final String name;
        private final String description;
        private final String example;

        public ErrorSubtypeInfo(String code, String name, String description, String example) {
            this.code = code;
            this.name = name;
            this.description = description;
            this.example = example;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getExample() { return example; }
    }

    // Header Error Subtypes (TYP-001)
    private static final List<ErrorSubtypeInfo> HEADER_ERROR_SUBTYPES = Arrays.asList(
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_MISSING,
                    ErrorCodeConstants.SUBTYPE_MISSING,
                    "Encabezado requerido faltante en la solicitud",
                    "Missing x-correlation-id header"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_TOO_SHORT,
                    ErrorCodeConstants.SUBTYPE_TOO_SHORT,
                    "Valor del encabezado demasiado corto",
                    "Header x-correlation-id must be at least 36 characters"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_TOO_LONG,
                    ErrorCodeConstants.SUBTYPE_TOO_LONG,
                    "Valor del encabezado excede la longitud máxima",
                    "Header x-correlation-id exceeds maximum length of 36 characters"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_INVALID_FORMAT,
                    ErrorCodeConstants.SUBTYPE_INVALID_FORMAT,
                    "Formato del encabezado inválido",
                    "Header x-correlation-id must be a valid UUID format"
            )
    );

    // Resource Not Found Subtypes (TYP-002)
    private static final List<ErrorSubtypeInfo> RESOURCE_NOT_FOUND_SUBTYPES = Arrays.asList(
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_NOT_FOUND_BY_ID,
                    ErrorCodeConstants.SUBTYPE_NOT_FOUND_BY_ID,
                    "Recurso no encontrado por ID especificado",
                    "Client with ID 999 not found"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_NOT_FOUND_AFTER_OPERATION,
                    ErrorCodeConstants.SUBTYPE_NOT_FOUND_AFTER_OPERATION,
                    "Recurso no encontrado después de una operación",
                    "TypeDocument not found after update operation"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_ENDPOINT_NOT_FOUND,
                    ErrorCodeConstants.SUBTYPE_ENDPOINT_NOT_FOUND,
                    "Endpoint o ruta no existe en la API",
                    "The requested endpoint /api/invalid does not exist"
            )
    );

    // Validation Error Subtypes (TYP-003)
    private static final List<ErrorSubtypeInfo> VALIDATION_ERROR_SUBTYPES = Arrays.asList(
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_FIELD_REQUIRED,
                    ErrorCodeConstants.SUBTYPE_FIELD_REQUIRED,
                    "Campo requerido faltante en el cuerpo de la solicitud",
                    "Field 'name' is required"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_FIELD_EMPTY,
                    ErrorCodeConstants.SUBTYPE_FIELD_EMPTY,
                    "Campo enviado con valor vacío",
                    "Field 'name' cannot be empty"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_LENGTH_TOO_SHORT,
                    ErrorCodeConstants.SUBTYPE_LENGTH_TOO_SHORT,
                    "Longitud del campo por debajo del mínimo requerido",
                    "Name must be at least 3 characters"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_LENGTH_TOO_LONG,
                    ErrorCodeConstants.SUBTYPE_LENGTH_TOO_LONG,
                    "Longitud del campo excede el máximo permitido",
                    "Name exceeds maximum length of 70 characters"
            ),
            new ErrorSubtypeInfo(
                    ErrorCodeConstants.SUBTYPE_CODE_DUPLICATE_VALUE,
                    ErrorCodeConstants.SUBTYPE_DUPLICATE_VALUE,
                    "Valor duplicado detectado (violación de restricción única)",
                    "Client with email 'juan.perez@example.com' already exists"
            )
    );

    // All error types
    private static final Map<String, ErrorCodeInfo> ERROR_CATALOG = new LinkedHashMap<>();

    static {
        // TYP-001: Header Errors
        ERROR_CATALOG.put(ErrorCodeConstants.TYPE_CODE_HEADER, new ErrorCodeInfo(
                ErrorCodeConstants.TYPE_CODE_HEADER,
                ErrorCodeConstants.TYPE_HEADER,
                "Errores relacionados con encabezados HTTP requeridos o mal formateados",
                400,
                ErrorCodeConstants.TYPE_CODE_HEADER,
                HEADER_ERROR_SUBTYPES
        ));

        // TYP-002: Resource Not Found Errors
        ERROR_CATALOG.put(ErrorCodeConstants.TYPE_CODE_RESOURCE, new ErrorCodeInfo(
                ErrorCodeConstants.TYPE_CODE_RESOURCE,
                ErrorCodeConstants.TYPE_RESOURCE,
                "Errores cuando un recurso solicitado no existe",
                404,
                ErrorCodeConstants.TYPE_CODE_RESOURCE,
                RESOURCE_NOT_FOUND_SUBTYPES
        ));

        // TYP-003: Validation Errors
        ERROR_CATALOG.put(ErrorCodeConstants.TYPE_CODE_VALIDATION, new ErrorCodeInfo(
                ErrorCodeConstants.TYPE_CODE_VALIDATION,
                ErrorCodeConstants.TYPE_VALIDATION,
                "Errores de validación del cuerpo de la solicitud",
                400, // Can also be 409 for duplicates
                ErrorCodeConstants.TYPE_CODE_VALIDATION,
                VALIDATION_ERROR_SUBTYPES
        ));
    }

    /**
     * Gets all registered error types.
     * @return Unmodifiable map of error code to ErrorCodeInfo
     */
    public static Map<String, ErrorCodeInfo> getAllErrorTypes() {
        return Collections.unmodifiableMap(ERROR_CATALOG);
    }

    /**
     * Gets error information by type code.
     * @param typeCode The type code (e.g., "TYP-001")
     * @return Optional containing ErrorCodeInfo if found
     */
    public static Optional<ErrorCodeInfo> getErrorByTypeCode(String typeCode) {
        return Optional.ofNullable(ERROR_CATALOG.get(typeCode));
    }

    /**
     * Gets all error subtypes for a specific type code.
     * @param typeCode The type code (e.g., "TYP-001")
     * @return List of subtypes or empty list if type not found
     */
    public static List<ErrorSubtypeInfo> getSubtypesByTypeCode(String typeCode) {
        return getErrorByTypeCode(typeCode)
                .map(ErrorCodeInfo::getSubtypes)
                .orElse(Collections.emptyList());
    }

    /**
     * Generates a formatted documentation string for a specific error type.
     * Useful for API response descriptions.
     * @param typeCode The type code
     * @return Formatted documentation string
     */
    public static String getFormattedDocumentation(String typeCode) {
        return getErrorByTypeCode(typeCode)
                .map(errorInfo -> {
                    StringBuilder doc = new StringBuilder();
                    doc.append("**Error Type:** ").append(errorInfo.getCode())
                       .append(" (").append(errorInfo.getName()).append(")")
                       .append("\n\n**Description:** ").append(errorInfo.getDescription())
                       .append("\n\n**HTTP Status:** ").append(errorInfo.getHttpStatus())
                       .append("\n\n**Posibles Subcódigos:**\n\n");
                    
                    for (ErrorSubtypeInfo subtype : errorInfo.getSubtypes()) {
                        doc.append("- `").append(subtype.getCode()).append("` - ")
                           .append(subtype.getName()).append(": ")
                           .append(subtype.getDescription())
                           .append("\n");
                    }
                    
                    return doc.toString();
                })
                .orElse("Error type not found");
    }

    /**
     * Generates a compact list of error codes for API response description.
     * @param typeCodes Variable number of type codes to include
     * @return Formatted string with all error codes and subtypes
     */
    public static String getCompactErrorCodesList(String... typeCodes) {
        StringBuilder result = new StringBuilder("\n\n**Códigos de Error Posibles:**\n\n");
        
        for (String typeCode : typeCodes) {
            getErrorByTypeCode(typeCode).ifPresent(errorInfo -> {
                result.append("**").append(errorInfo.getCode()).append("** - ")
                      .append(errorInfo.getName()).append(" (HTTP ").append(errorInfo.getHttpStatus()).append(")")
                      .append("\n");
                
                for (ErrorSubtypeInfo subtype : errorInfo.getSubtypes()) {
                    result.append("  - `").append(subtype.getCode()).append("`: ")
                          .append(subtype.getDescription()).append("\n");
                }
                result.append("\n");
            });
        }
        
        return result.toString();
    }

    /**
     * Gets a description enriched with error codes for Swagger @ApiResponse.
     * @param baseDescription Base description for the response
     * @param typeCodes Error type codes to document
     * @return Enhanced description with error codes
     */
    public static String enrichDescription(String baseDescription, String... typeCodes) {
        return baseDescription + getCompactErrorCodesList(typeCodes);
    }

    private ErrorCodeDocumentation() {
        // Private constructor to prevent instantiation
    }
}
