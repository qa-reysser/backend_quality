package com.quality.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenAPI/Swagger configuration.
 * Configures API documentation with reusable components (schemas, examples, responses).
 * Follows Single Responsibility Principle - only configures OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(createComponents());
    }

    /**
     * API Information and metadata.
     * Includes comprehensive error code documentation for QA team.
     */
    private Info apiInfo() {
        return new Info()
                .title("Gestión de Clientes y Tipos de Documento API")
                .version("1.0.0")
                .description(buildApiDescription())
                .contact(new Contact()
                        .name("Quality Team")
                        .email("quality@example.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"));
    }

    /**
     * Builds API description.
     */
    private String buildApiDescription() {
        return "API RESTful para la gestión completa de clientes y tipos de documento mediante operaciones CRUD. " +
               "Incluye validación de encabezados personalizados, validación de unicidad (email, documento), " +
               "relaciones entre entidades, manejo estructurado de errores y enlaces HATEOAS.";
    }

    /**
     * Creates reusable OpenAPI components.
     * Includes schemas, examples, and responses following DRY principle.
     */
    private Components createComponents() {
        Components components = new Components();
        
        // Add error schemas
        addErrorSchemas(components);
        
        // Add error examples
        addErrorExamples(components);
        
        // Add reusable responses
        addReusableResponses(components);
        
        return components;
    }

    /**
     * Defines error response schemas.
     */
    private void addErrorSchemas(Components components) {
        // Schema for error details info
        Schema<?> errorDetailsInfoSchema = new Schema<>()
                .type("object")
                .addProperty("problematicField", new Schema<>().type("string").description("Campo que causó el error"))
                .addProperty("invalidValue", new Schema<>().type("string").description("Valor inválido proporcionado"))
                .addProperty("correctFormat", new Schema<>().type("string").description("Formato correcto esperado"));

        // Schema for error links
        Schema<?> errorLinkSchema = new Schema<>()
                .type("object")
                .addProperty("href", new Schema<>().type("string").description("URL del enlace"))
                .addProperty("method", new Schema<>().type("string").description("Método HTTP"));

        // Schema for complete error detail
        Schema<?> errorDetailSchema = new Schema<>()
                .type("object")
                .addProperty("timestamp", new Schema<>().type("string").format("date-time").description("Momento del error"))
                .addProperty("status", new Schema<>().type("integer").description("Código de estado HTTP"))
                .addProperty("error", new Schema<>().type("string").description("Tipo de error HTTP"))
                .addProperty("message", new Schema<>().type("string").description("Mensaje descriptivo del error"))
                .addProperty("typeCode", new Schema<>().type("string").description("Código del tipo de error"))
                .addProperty("type", new Schema<>().type("string").description("Descripción del tipo de error"))
                .addProperty("subtypeCode", new Schema<>().type("string").description("Código del subtipo de error"))
                .addProperty("subtype", new Schema<>().type("string").description("Descripción del subtipo de error"))
                .addProperty("details", errorDetailsInfoSchema)
                .addProperty("path", new Schema<>().type("string").description("Ruta del endpoint"))
                .addProperty("documentationUrl", new Schema<>().type("string").description("URL de documentación del error"))
                .addProperty("_links", new Schema<>().type("object").additionalProperties(errorLinkSchema));

        // Main error response schema - sin nombre de clase para que muestre directamente el objeto
        Schema<?> errorResponseSchema = new Schema<>()
                .type("object")
                .name(null)  // Evita mostrar "ErrorResponse{" en Swagger UI
                .title(null)
                .addProperty("errors", errorDetailSchema);

        components.addSchemas("ErrorResponse", errorResponseSchema);
        components.addSchemas("ErrorDetail", errorDetailSchema);
        components.addSchemas("ErrorDetailsInfo", errorDetailsInfoSchema);
    }

    /**
     * Defines reusable error examples.
     */
    private void addErrorExamples(Components components) {
        // Header validation error example - Usando Map en lugar de String JSON
        Map<String, Object> headerErrorMap = new LinkedHashMap<>();
        Map<String, Object> headerErrorDetail = new LinkedHashMap<>();
        headerErrorDetail.put("timestamp", "2025-11-13T10:30:00.000000000");
        headerErrorDetail.put("status", 400);
        headerErrorDetail.put("error", "Bad Request");
        headerErrorDetail.put("message", "Missing x-correlation-id header");
        headerErrorDetail.put("typeCode", "TYP-001");
        headerErrorDetail.put("type", "header_error");
        headerErrorDetail.put("subtypeCode", "HDR-001");
        headerErrorDetail.put("subtype", "missing_header");
        
        Map<String, Object> headerDetails = new LinkedHashMap<>();
        headerDetails.put("problematicField", "x-correlation-id");
        headerDetails.put("invalidValue", "Header value is missing or null");
        headerDetails.put("correctFormat", "The value should be a valid UUID with exactly 36 characters.");
        headerErrorDetail.put("details", headerDetails);
        
        headerErrorDetail.put("path", "/priorities");
        headerErrorDetail.put("documentationUrl", "http://localhost:8080/api/docs#/HDR-001");
        
        Map<String, Object> headerLinks = new LinkedHashMap<>();
        Map<String, String> selfLink = new LinkedHashMap<>();
        selfLink.put("href", "/priorities");
        selfLink.put("method", "GET");
        Map<String, String> docLink = new LinkedHashMap<>();
        docLink.put("href", "http://localhost:8080/api/docs#/HDR-001");
        headerLinks.put("self", selfLink);
        headerLinks.put("documentation", docLink);
        headerErrorDetail.put("_links", headerLinks);
        
        headerErrorMap.put("errors", headerErrorDetail);
        
        components.addExamples("HeaderValidationError", new Example()
                .summary("Error de validación de encabezado")
                .description("Ejemplo cuando falta un encabezado requerido")
                .value(headerErrorMap));

        // Resource not found error example
        Map<String, Object> notFoundErrorMap = new LinkedHashMap<>();
        Map<String, Object> notFoundErrorDetail = new LinkedHashMap<>();
        notFoundErrorDetail.put("timestamp", "2025-11-13T10:30:00.000000000");
        notFoundErrorDetail.put("status", 404);
        notFoundErrorDetail.put("error", "Not Found");
        notFoundErrorDetail.put("message", "Resource with ID 999 not found");
        notFoundErrorDetail.put("typeCode", "TYP-002");
        notFoundErrorDetail.put("type", "resource_not_found");
        notFoundErrorDetail.put("subtypeCode", "RNF-001");
        notFoundErrorDetail.put("subtype", "resource_not_found_by_id");
        
        Map<String, Object> notFoundDetails = new LinkedHashMap<>();
        notFoundDetails.put("problematicField", "id");
        notFoundDetails.put("invalidValue", "999");
        notFoundDetails.put("correctFormat", "ID must exist in the database");
        notFoundErrorDetail.put("details", notFoundDetails);
        
        notFoundErrorDetail.put("path", "/clients/999");
        notFoundErrorDetail.put("documentationUrl", "http://localhost:8080/api/docs#/RNF-001");
        
        Map<String, Object> notFoundLinks = new LinkedHashMap<>();
        Map<String, String> notFoundSelf = new LinkedHashMap<>();
        notFoundSelf.put("href", "/clients/999");
        notFoundSelf.put("method", "GET");
        Map<String, String> listLink = new LinkedHashMap<>();
        listLink.put("href", "/clients");
        listLink.put("method", "GET");
        notFoundLinks.put("self", notFoundSelf);
        notFoundLinks.put("list", listLink);
        notFoundErrorDetail.put("_links", notFoundLinks);
        
        notFoundErrorMap.put("errors", notFoundErrorDetail);
        
        components.addExamples("ResourceNotFoundError", new Example()
                .summary("Recurso no encontrado")
                .description("Ejemplo cuando el ID no existe")
                .value(notFoundErrorMap));

        // Validation error example
        Map<String, Object> validationErrorMap = new LinkedHashMap<>();
        Map<String, Object> validationErrorDetail = new LinkedHashMap<>();
        validationErrorDetail.put("timestamp", "2025-11-13T10:30:00.000000000");
        validationErrorDetail.put("status", 400);
        validationErrorDetail.put("error", "Bad Request");
        validationErrorDetail.put("message", "Validation failed for field 'name': Name must be between 3 and 70 characters");
        validationErrorDetail.put("typeCode", "TYP-003");
        validationErrorDetail.put("type", "request_body_validation_error");
        validationErrorDetail.put("subtypeCode", "RBV-003");
        validationErrorDetail.put("subtype", "field_length_below_minimum");
        
        Map<String, Object> validationDetails = new LinkedHashMap<>();
        validationDetails.put("problematicField", "name");
        validationDetails.put("invalidValue", "AB");
        validationDetails.put("correctFormat", "Name must be between 3 and 70 characters");
        validationErrorDetail.put("details", validationDetails);
        
        validationErrorDetail.put("path", "/clients");
        validationErrorDetail.put("documentationUrl", "http://localhost:8080/api/docs#/RBV-003");
        
        Map<String, Object> validationLinks = new LinkedHashMap<>();
        Map<String, String> validationSelf = new LinkedHashMap<>();
        validationSelf.put("href", "/clients");
        validationSelf.put("method", "POST");
        validationLinks.put("self", validationSelf);
        validationErrorDetail.put("_links", validationLinks);
        
        validationErrorMap.put("errors", validationErrorDetail);
        
        components.addExamples("ValidationError", new Example()
                .summary("Error de validación de datos")
                .description("Ejemplo cuando los datos no cumplen las restricciones")
                .value(validationErrorMap));

        // Conflict error example (duplicate name)
        Map<String, Object> conflictErrorMap = new LinkedHashMap<>();
        Map<String, Object> conflictErrorDetail = new LinkedHashMap<>();
        conflictErrorDetail.put("timestamp", "2025-11-13T10:30:00.000000000");
        conflictErrorDetail.put("status", 409);
        conflictErrorDetail.put("error", "Conflict");
        conflictErrorDetail.put("message", "Duplicate field value detected");
        conflictErrorDetail.put("typeCode", "TYP-003");
        conflictErrorDetail.put("type", "request_body_validation_error");
        conflictErrorDetail.put("subtypeCode", "RBV-005");
        conflictErrorDetail.put("subtype", "duplicate_value_detected");
        
        Map<String, Object> conflictDetails = new LinkedHashMap<>();
        conflictDetails.put("problematicField", "field");
        conflictDetails.put("invalidValue", "value");
        conflictDetails.put("correctFormat", "Value must be unique");
        conflictErrorDetail.put("details", conflictDetails);
        
        conflictErrorDetail.put("path", "/clients");
        conflictErrorDetail.put("documentationUrl", "http://localhost:8080/api/docs#/RBV-005");
        
        Map<String, Object> conflictLinks = new LinkedHashMap<>();
        Map<String, String> conflictSelf = new LinkedHashMap<>();
        conflictSelf.put("href", "/priorities");
        conflictSelf.put("method", "POST");
        Map<String, String> conflictList = new LinkedHashMap<>();
        conflictList.put("href", "/priorities");
        conflictList.put("method", "GET");
        conflictLinks.put("self", conflictSelf);
        conflictLinks.put("list", conflictList);
        conflictErrorDetail.put("_links", conflictLinks);
        
        conflictErrorMap.put("errors", conflictErrorDetail);
        
        components.addExamples("ConflictError", new Example()
                .summary("Conflicto - Nombre duplicado")
                .description("Ejemplo cuando se intenta crear/actualizar con un nombre que ya existe")
                .value(conflictErrorMap));
    }

    /**
     * Defines reusable API responses.
     * Following DRY principle - define once, reference everywhere.
     */
    private void addReusableResponses(Components components) {
        // 400 Bad Request - Header validation
        ApiResponse badRequestHeader = new ApiResponse()
                .description("Encabezados requeridos faltantes o inválidos")
                .content(new Content()
                        .addMediaType("application/json", 
                                new MediaType()
                                        .schema(new Schema<>()
                                                .type("object")
                                                .$ref("#/components/schemas/ErrorResponse"))
                                        .addExamples("headerError", 
                                                new Example().$ref("#/components/examples/HeaderValidationError"))));
        components.addResponses("BadRequest_HeaderValidation", badRequestHeader);

        // 400 Bad Request - Validation error
        ApiResponse badRequestValidation = new ApiResponse()
                .description("Datos de validación incorrectos")
                .content(new Content()
                        .addMediaType("application/json", 
                                new MediaType()
                                        .schema(new Schema<>()
                                                .type("object")
                                                .$ref("#/components/schemas/ErrorResponse"))
                                        .addExamples("validationError", 
                                                new Example().$ref("#/components/examples/ValidationError"))));
        components.addResponses("BadRequest_Validation", badRequestValidation);

        // 404 Not Found
        ApiResponse notFound = new ApiResponse()
                .description("Recurso no encontrado")
                .content(new Content()
                        .addMediaType("application/json", 
                                new MediaType()
                                        .schema(new Schema<>()
                                                .type("object")
                                                .$ref("#/components/schemas/ErrorResponse"))
                                        .addExamples("notFoundError", 
                                                new Example().$ref("#/components/examples/ResourceNotFoundError"))));
        components.addResponses("NotFound", notFound);

        // 409 Conflict
        ApiResponse conflict = new ApiResponse()
                .description("Conflicto - Nombre duplicado")
                .content(new Content()
                        .addMediaType("application/json", 
                                new MediaType()
                                        .schema(new Schema<>()
                                                .type("object")
                                                .$ref("#/components/schemas/ErrorResponse"))
                                        .addExamples("conflictError", 
                                                new Example().$ref("#/components/examples/ConflictError"))));
        components.addResponses("Conflict", conflict);
    }
}
