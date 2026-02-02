package com.quality.config;

/**
 * Centralized Swagger examples for error responses.
 * Provides JSON examples for documentation following DRY principle.
 * All examples are defined as constants for reusability across controllers.
 */
public class SwaggerExamples {

    /**
     * Example for 400 Bad Request - Header Validation Error
     */
    public static final String HEADER_VALIDATION_ERROR = """
            {
              "errors": {
                "timestamp": "2025-11-13T10:30:00.000000000",
                "status": 400,
                "error": "Bad Request",
                "message": "Missing x-correlation-id header",
                "typeCode": "TYP-001",
                "type": "header_error",
                "subtypeCode": "HDR-001",
                "subtype": "missing_header",
                "details": {
                  "problematicField": "x-correlation-id",
                  "invalidValue": "Header value is missing or null",
                  "correctFormat": "The value should be a valid UUID with exactly 36 characters."
                },
                "path": "/clients",
                "documentationUrl": "http://localhost:8080/api/docs#/HDR-001",
                "_links": {
                  "self": {
                    "href": "/clients",
                    "method": "GET"
                  },
                  "documentation": {
                    "href": "http://localhost:8080/api/docs#/HDR-001"
                  }
                }
              }
            }""";

    /**
     * Example for 404 Not Found - Resource Not Found Error (Generic)
     */
    public static final String RESOURCE_NOT_FOUND_ERROR = """
            {
              "errors": {
                "timestamp": "2025-11-13T10:30:00.000000000",
                "status": 404,
                "error": "Not Found",
                "message": "Resource with ID 999 not found",
                "typeCode": "TYP-002",
                "type": "resource_not_found",
                "subtypeCode": "RNF-001",
                "subtype": "resource_not_found_by_id",
                "details": {
                  "problematicField": "id",
                  "invalidValue": "999",
                  "correctFormat": "ID must exist in the database"
                },
                "path": "/clients/999",
                "documentationUrl": "http://localhost:8080/api/docs#/RNF-001",
                "_links": {
                  "self": {
                    "href": "/clients/999",
                    "method": "GET"
                  },
                  "list": {
                    "href": "/clients",
                    "method": "GET"
                  }
                }
              }
            }""";

    /**
     * Example for 404 Not Found - Client Not Found
     */
    public static final String CLIENT_NOT_FOUND_ERROR = """
            {
              "errors": {
                "timestamp": "2026-02-02T10:30:00.000000000",
                "status": 404,
                "error": "Not Found",
                "message": "Client with ID 999 not found",
                "typeCode": "TYP-002",
                "type": "resource_not_found",
                "subtypeCode": "RNF-001",
                "subtype": "resource_not_found_by_id",
                "details": {
                  "problematicField": "id",
                  "invalidValue": "999",
                  "correctFormat": "ID must exist in the database"
                },
                "path": "/clients/999",
                "documentationUrl": "http://localhost:8080/api/docs#/RNF-001",
                "_links": {
                  "self": {
                    "href": "/clients/999",
                    "method": "GET"
                  },
                  "list": {
                    "href": "/clients",
                    "method": "GET"
                  }
                }
              }
            }""";

    /**
     * Example for 404 Not Found - TypeDocument Not Found
     */
    public static final String TYPE_DOCUMENT_NOT_FOUND_ERROR = """
            {
              "errors": {
                "timestamp": "2026-02-02T10:30:00.000000000",
                "status": 404,
                "error": "Not Found",
                "message": "TypeDocument with ID 50 not found",
                "typeCode": "TYP-002",
                "type": "resource_not_found",
                "subtypeCode": "RNF-001",
                "subtype": "resource_not_found_by_id",
                "details": {
                  "problematicField": "idTypeDocument",
                  "invalidValue": "50",
                  "correctFormat": "TypeDocument ID must exist in the database"
                },
                "path": "/clients",
                "documentationUrl": "http://localhost:8080/api/docs#/RNF-001",
                "_links": {
                  "self": {
                    "href": "/clients",
                    "method": "POST"
                  },
                  "typeDocuments": {
                    "href": "/type-documents",
                    "method": "GET"
                  }
                }
              }
            }""";

    /**
     * Example for 400 Bad Request - Validation Error (Generic)
     */
    public static final String VALIDATION_ERROR = """
            {
              "errors": {
                "timestamp": "2025-11-13T10:30:00.000000000",
                "status": 400,
                "error": "Bad Request",
                "message": "Validation failed for field 'name': Name must be between 3 and 70 characters",
                "typeCode": "TYP-003",
                "type": "request_body_validation_error",
                "subtypeCode": "RBV-003",
                "subtype": "field_length_below_minimum",
                "details": {
                  "problematicField": "name",
                  "invalidValue": "AB",
                  "correctFormat": "Name must be between 3 and 70 characters"
                },
                "path": "/type-documents",
                "documentationUrl": "http://localhost:8080/api/docs#/RBV-003",
                "_links": {
                  "self": {
                    "href": "/type-documents",
                    "method": "POST"
                  }
                }
              }
            }""";

    /**
     * Example for 400 Bad Request - Client Validation Error
     */
    public static final String CLIENT_VALIDATION_ERROR = """
            {
              "errors": {
                "timestamp": "2026-02-02T10:30:00.000000000",
                "status": 400,
                "error": "Bad Request",
                "message": "Validation failed for field 'email': Email must be valid",
                "typeCode": "TYP-003",
                "type": "request_body_validation_error",
                "subtypeCode": "RBV-001",
                "subtype": "invalid_field_format",
                "details": {
                  "problematicField": "email",
                  "invalidValue": "invalid-email",
                  "correctFormat": "Must be a valid email format (example@domain.com)"
                },
                "path": "/clients",
                "documentationUrl": "http://localhost:8080/api/docs#/RBV-001",
                "_links": {
                  "self": {
                    "href": "/clients",
                    "method": "POST"
                  }
                }
              }
            }""";

    /**
     * Example for 409 Conflict - Duplicate Value Error (Generic)
     */
    public static final String CONFLICT_ERROR = """
            {
              "errors": {
                "timestamp": "2025-11-13T10:30:00.000000000",
                "status": 409,
                "error": "Conflict",
                "message": "Duplicate field detected",
                "typeCode": "TYP-003",
                "type": "request_body_validation_error",
                "subtypeCode": "RBV-005",
                "subtype": "duplicate_value_detected",
                "details": {
                  "problematicField": "field",
                  "invalidValue": "value",
                  "correctFormat": "Value must be unique"
                },
                "path": "/clients",
                "documentationUrl": "http://localhost:8080/api/docs#/RBV-005",
                "_links": {
                  "self": {
                    "href": "/clients",
                    "method": "POST"
                  },
                  "list": {
                    "href": "/clients",
                    "method": "GET"
                  }
                }
              }
            }""";

    /**
     * Example for 409 Conflict - Duplicate Email Error
     */
    public static final String CLIENT_DUPLICATE_EMAIL_ERROR = """
            {
              "errors": {
                "timestamp": "2026-02-02T10:30:00.000000000",
                "status": 409,
                "error": "Conflict",
                "message": "Client with email 'juan.perez@example.com' already exists",
                "typeCode": "TYP-003",
                "type": "request_body_validation_error",
                "subtypeCode": "RBV-005",
                "subtype": "duplicate_value_detected",
                "details": {
                  "problematicField": "email",
                  "invalidValue": "juan.perez@example.com",
                  "correctFormat": "Email must be unique in the system"
                },
                "path": "/clients",
                "documentationUrl": "http://localhost:8080/api/docs#/RBV-005",
                "_links": {
                  "self": {
                    "href": "/clients",
                    "method": "POST"
                  },
                  "list": {
                    "href": "/clients",
                    "method": "GET"
                  }
                }
              }
            }""";

    /**
     * Example for 409 Conflict - Duplicate Document Number Error
     */
    public static final String CLIENT_DUPLICATE_DOCUMENT_ERROR = """
            {
              "errors": {
                "timestamp": "2026-02-02T10:30:00.000000000",
                "status": 409,
                "error": "Conflict",
                "message": "Client with documentNumber '12345678' already exists",
                "typeCode": "TYP-003",
                "type": "request_body_validation_error",
                "subtypeCode": "RBV-005",
                "subtype": "duplicate_value_detected",
                "details": {
                  "problematicField": "documentNumber",
                  "invalidValue": "12345678",
                  "correctFormat": "Document number must be unique in the system"
                },
                "path": "/clients",
                "documentationUrl": "http://localhost:8080/api/docs#/RBV-005",
                "_links": {
                  "self": {
                    "href": "/clients",
                    "method": "POST"
                  },
                  "list": {
                    "href": "/clients",
                    "method": "GET"
                  }
                }
              }
            }""";

    /**
     * Example for 409 Conflict - Duplicate TypeDocument Code Error
     */
    public static final String TYPE_DOCUMENT_DUPLICATE_CODE_ERROR = """
            {
              "errors": {
                "timestamp": "2026-02-02T10:30:00.000000000",
                "status": 409,
                "error": "Conflict",
                "message": "TypeDocument with code 'DNI' already exists",
                "typeCode": "TYP-003",
                "type": "request_body_validation_error",
                "subtypeCode": "RBV-005",
                "subtype": "duplicate_value_detected",
                "details": {
                  "problematicField": "code",
                  "invalidValue": "DNI",
                  "correctFormat": "Code must be unique in the system"
                },
                "path": "/type-documents",
                "documentationUrl": "http://localhost:8080/api/docs#/RBV-005",
                "_links": {
                  "self": {
                    "href": "/type-documents",
                    "method": "POST"
                  },
                  "list": {
                    "href": "/type-documents",
                    "method": "GET"
                  }
                }
              }
            }""";

    private SwaggerExamples() {
        // Private constructor to prevent instantiation
    }
}
