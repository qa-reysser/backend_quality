package com.quality.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a link in the error response (HATEOAS).
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Enlace HATEOAS")
public class ErrorLink {
    @Schema(description = "URL del enlace", example = "/priorities")
    private String href;
    
    @Schema(description = "Método HTTP", example = "GET")
    private String method;
}
