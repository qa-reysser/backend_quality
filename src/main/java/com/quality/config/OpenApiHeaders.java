package com.quality.config;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to document required headers in OpenAPI/Swagger.
 * Consolidates header documentation following DRY principle.
 * Apply this annotation at method level to document all required headers.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
    name = "x-correlation-id",
    description = "Identificador único para correlación entre servicios. UUID v4 válido con 36 caracteres.",
    required = true,
    in = ParameterIn.HEADER,
    schema = @Schema(type = "string", format = "uuid", example = "550e8400-e29b-41d4-a716-446655440000")
)
@Parameter(
    name = "x-request-id",
    description = "Identificador único de la solicitud. UUID v4 válido con 36 caracteres.",
    required = true,
    in = ParameterIn.HEADER,
    schema = @Schema(type = "string", format = "uuid", example = "6ba7b810-9dad-11d1-80b4-00c04fd430c8")
)
@Parameter(
    name = "x-transaction-id",
    description = "Identificador único de la transacción de negocio. UUID v4 válido con 36 caracteres.",
    required = true,
    in = ParameterIn.HEADER,
    schema = @Schema(type = "string", format = "uuid", example = "7c9e6679-7425-40de-944b-e07fc1f90ae7")
)
public @interface OpenApiHeaders {
}
