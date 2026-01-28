package com.quality.exception.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Detailed information about the error.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "Información detallada sobre el campo erróneo")
public class ErrorDetailsInfo {
    @Schema(description = "Campo que causó el error", example = "x-correlation-id")
    private String problematicField;
    
    @Schema(description = "Valor inválido proporcionado", example = "Header value is missing or null")
    private String invalidValue;
    
    @Schema(description = "Formato correcto esperado", example = "The value should be a valid UUID with exactly 36 characters.")
    private String correctFormat;
}
