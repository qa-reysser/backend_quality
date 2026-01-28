package com.quality.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Complete error detail structure.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "Detalle completo de un error")
public class ErrorDetail {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    @Schema(description = "Fecha y hora del error", example = "2025-11-13T10:30:00.000000000")
    private LocalDateTime timestamp;
    
    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;
    
    @Schema(description = "Tipo de error HTTP", example = "Bad Request")
    private String error;
    
    @Schema(description = "Mensaje descriptivo del error", example = "Missing x-correlation-id header")
    private String message;
    
    @Schema(description = "Código del tipo de error", example = "TYP-001")
    private String typeCode;
    
    @Schema(description = "Tipo de error", example = "header_error")
    private String type;
    
    @Schema(description = "Código del subtipo de error", example = "HDR-001")
    private String subtypeCode;
    
    @Schema(description = "Subtipo de error", example = "missing_header")
    private String subtype;
    
    @Schema(description = "Información detallada del error")
    private ErrorDetailsInfo details;
    
    @Schema(description = "Ruta del endpoint", example = "/priorities")
    private String path;
    
    @Schema(description = "URL de documentación del error", example = "http://localhost:8080/api/docs#/HDR-001")
    private String documentationUrl;

    @JsonProperty("_links")
    @Schema(description = "Enlaces HATEOAS")
    private Map<String, ErrorLink> links;
}
