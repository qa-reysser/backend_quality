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
                "path": "/priorities",
                "documentationUrl": "http://localhost:8080/api/docs#/HDR-001",
                "_links": {
                  "self": {
                    "href": "/priorities",
                    "method": "GET"
                  },
                  "documentation": {
                    "href": "http://localhost:8080/api/docs#/HDR-001"
                  }
                }
              }
            }""";

    /**
     * Example for 404 Not Found - Resource Not Found Error
     */
    public static final String RESOURCE_NOT_FOUND_ERROR = """
            {
              "errors": {
                "timestamp": "2025-11-13T10:30:00.000000000",
                "status": 404,
                "error": "Not Found",
                "message": "Priority with ID 999 not found",
                "typeCode": "TYP-002",
                "type": "resource_not_found",
                "subtypeCode": "RNF-001",
                "subtype": "resource_not_found_by_id",
                "details": {
                  "problematicField": "id",
                  "invalidValue": "999",
                  "correctFormat": "ID must exist in the database"
                },
                "path": "/priorities/999",
                "documentationUrl": "http://localhost:8080/api/docs#/RNF-001",
                "_links": {
                  "self": {
                    "href": "/priorities/999",
                    "method": "GET"
                  },
                  "list": {
                    "href": "/priorities",
                    "method": "GET"
                  }
                }
              }
            }""";

    /**
     * Example for 400 Bad Request - Validation Error
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
                "path": "/priorities",
                "documentationUrl": "http://localhost:8080/api/docs#/RBV-003",
                "_links": {
                  "self": {
                    "href": "/priorities",
                    "method": "POST"
                  }
                }
              }
            }""";

    /**
     * Example for 409 Conflict - Duplicate Value Error
     */
    public static final String CONFLICT_ERROR = """
            {
              "errors": {
                "timestamp": "2025-11-13T10:30:00.000000000",
                "status": 409,
                "error": "Conflict",
                "message": "Priority with name 'Alta' already exists",
                "typeCode": "TYP-003",
                "type": "request_body_validation_error",
                "subtypeCode": "RBV-005",
                "subtype": "duplicate_value_detected",
                "details": {
                  "problematicField": "name",
                  "invalidValue": "Alta",
                  "correctFormat": "Name must be unique"
                },
                "path": "/priorities",
                "documentationUrl": "http://localhost:8080/api/docs#/RBV-005",
                "_links": {
                  "self": {
                    "href": "/priorities",
                    "method": "POST"
                  },
                  "list": {
                    "href": "/priorities",
                    "method": "GET"
                  }
                }
              }
            }""";

    private SwaggerExamples() {
        // Private constructor to prevent instantiation
    }
}
