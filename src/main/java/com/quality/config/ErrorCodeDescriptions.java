package com.quality.config;

/**
 * Centralized constants for API error code documentation.
 * Provides pre-formatted documentation strings for Swagger annotations.
 * Follows DRY principle - define once, reference everywhere.
 * All values are compile-time constants for use in annotations.
 */
public class ErrorCodeDescriptions {

    // ========== Header Error Documentation (TYP-001) ==========
    
    public static final String HEADER_ERROR_CODES = """
            
            **Códigos de Error Posibles:**
            
            **TYP-001** - header_error (HTTP 400)
              - `HDR-001`: Encabezado requerido faltante en la solicitud
              - `HDR-002`: Valor del encabezado demasiado corto
              - `HDR-003`: Valor del encabezado excede la longitud máxima
              - `HDR-004`: Formato del encabezado inválido
            """;

    // ========== Resource Not Found Documentation (TYP-002) ==========
    
    public static final String RESOURCE_NOT_FOUND_CODES = """
            
            **Códigos de Error Posibles:**
            
            **TYP-002** - resource_not_found (HTTP 404)
              - `RNF-001`: Recurso no encontrado por ID especificado
              - `RNF-002`: Recurso no encontrado después de una operación
              - `RNF-003`: Endpoint o ruta no existe en la API
            """;

    // ========== Validation Error Documentation (TYP-003) ==========
    
    public static final String VALIDATION_ERROR_CODES = """
            
            **Códigos de Error Posibles:**
            
            **TYP-003** - request_body_validation_error (HTTP 400/409)
              - `RBV-001`: Campo requerido faltante en el cuerpo de la solicitud
              - `RBV-002`: Campo enviado con valor vacío
              - `RBV-003`: Longitud del campo por debajo del mínimo requerido
              - `RBV-004`: Longitud del campo excede el máximo permitido
              - `RBV-005`: Valor duplicado detectado (violación de restricción única)
            """;

    // ========== Combined Error Documentation for Endpoints ==========
    
    public static final String HEADER_ONLY = 
            "Encabezados requeridos faltantes o inválidos" + HEADER_ERROR_CODES;

    public static final String HEADER_AND_VALIDATION = 
            "Encabezados faltantes o datos de validación incorrectos" + 
            HEADER_ERROR_CODES + VALIDATION_ERROR_CODES;

    public static final String ALL_ERROR_CODES = 
            "Pueden ocurrir múltiples tipos de error" + 
            HEADER_ERROR_CODES + RESOURCE_NOT_FOUND_CODES + VALIDATION_ERROR_CODES;

    // ========== Resource-Specific Error Messages ==========
    
    public static final String CLIENT_NOT_FOUND_BY_ID = 
            "Cliente no encontrado - El ID especificado no existe en la base de datos" + 
            RESOURCE_NOT_FOUND_CODES;

    public static final String TYPE_DOCUMENT_NOT_FOUND_BY_ID = 
            "Tipo de documento no encontrado - El ID especificado no existe en la base de datos" + 
            RESOURCE_NOT_FOUND_CODES;

    public static final String TYPE_DOCUMENT_NOT_FOUND_IN_CLIENT = 
            "Tipo de documento no encontrado - El idTypeDocument especificado no existe. Debe crear el tipo de documento primero" + 
            RESOURCE_NOT_FOUND_CODES;

    public static final String CLIENT_CONFLICT_EMAIL = 
            "Conflicto - Email duplicado. Ya existe un cliente con ese email" + 
            VALIDATION_ERROR_CODES;

    public static final String CLIENT_CONFLICT_DOCUMENT = 
            "Conflicto - Número de documento duplicado. Ya existe un cliente con ese número de documento" + 
            VALIDATION_ERROR_CODES;

    public static final String TYPE_DOCUMENT_CONFLICT_CODE = 
            "Conflicto - Código duplicado. Ya existe un tipo de documento con ese código" + 
            VALIDATION_ERROR_CODES;

    // ========== Specific Endpoint Descriptions - TypeDocument ==========
    
    // GET /type-documents
    public static final String TYPE_DOC_GET_ALL_400 = HEADER_ONLY;

    // GET /type-documents/{id}
    public static final String TYPE_DOC_GET_BY_ID_400 = HEADER_ONLY;
    public static final String TYPE_DOC_GET_BY_ID_404 = TYPE_DOCUMENT_NOT_FOUND_BY_ID;

    // POST /type-documents
    public static final String TYPE_DOC_POST_400 = HEADER_AND_VALIDATION;
    public static final String TYPE_DOC_POST_409 = TYPE_DOCUMENT_CONFLICT_CODE;

    // PUT /type-documents/{id}
    public static final String TYPE_DOC_PUT_400 = HEADER_AND_VALIDATION;
    public static final String TYPE_DOC_PUT_404 = TYPE_DOCUMENT_NOT_FOUND_BY_ID;
    public static final String TYPE_DOC_PUT_409 = TYPE_DOCUMENT_CONFLICT_CODE;

    // DELETE /type-documents/{id}
    public static final String TYPE_DOC_DELETE_400 = HEADER_ONLY;
    public static final String TYPE_DOC_DELETE_404 = TYPE_DOCUMENT_NOT_FOUND_BY_ID;

    // ========== Specific Endpoint Descriptions - Client ==========
    
    // GET /clients
    public static final String CLIENT_GET_ALL_400 = HEADER_ONLY;

    // GET /clients/{id}
    public static final String CLIENT_GET_BY_ID_400 = HEADER_ONLY;
    public static final String CLIENT_GET_BY_ID_404 = CLIENT_NOT_FOUND_BY_ID;

    // POST /clients
    public static final String CLIENT_POST_400 = HEADER_AND_VALIDATION;
    public static final String CLIENT_POST_404 = TYPE_DOCUMENT_NOT_FOUND_IN_CLIENT;
    public static final String CLIENT_POST_409 = 
            "Conflicto - Email o número de documento duplicado. Ya existe un cliente con ese email o número de documento" + 
            VALIDATION_ERROR_CODES;

    // PUT /clients/{id}
    public static final String CLIENT_PUT_400 = HEADER_AND_VALIDATION;
    public static final String CLIENT_PUT_404_CLIENT = CLIENT_NOT_FOUND_BY_ID;
    public static final String CLIENT_PUT_404_TYPE_DOC = TYPE_DOCUMENT_NOT_FOUND_IN_CLIENT;
    public static final String CLIENT_PUT_409 = 
            "Conflicto - Email o número de documento duplicado. Ya existe un cliente con ese email o número de documento" + 
            VALIDATION_ERROR_CODES;

    // DELETE /clients/{id}
    public static final String CLIENT_DELETE_400 = HEADER_ONLY;
    public static final String CLIENT_DELETE_404 = CLIENT_NOT_FOUND_BY_ID;

    private ErrorCodeDescriptions() {
        // Private constructor to prevent instantiation
    }
}
