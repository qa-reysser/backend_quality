package com.quality.config;

import java.lang.annotation.*;

/**
 * Custom annotation to document error codes for API endpoints.
 * Used to enrich Swagger/OpenAPI documentation with detailed error code information.
 * Follows Open/Closed Principle - extends functionality without modifying existing code.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiErrorCodes {
    
    /**
     * Array of error type codes that this endpoint may return.
     * Example: {"TYP-001", "TYP-002"}
     */
    String[] value() default {};
    
    /**
     * Whether to include detailed subtype information in documentation.
     */
    boolean includeSubtypes() default true;
}
