package com.quality.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "DTO de Prioridad")
public class PriorityDTO {
    
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPriority;

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 70, message = "Name must be between 3 and 70 characters")
    @Schema(description = "Nombre de la prioridad (único)", example = "Alta", minLength = 3, maxLength = 70)
    private String name;

    @NotNull(message = "Description is required")
    @NotEmpty(message = "Description cannot be empty")
    @Size(min = 3, max = 70, message = "Description must be between 3 and 70 characters")
    @Schema(description = "Descripción de la prioridad", example = "Tareas de alta prioridad", minLength = 3, maxLength = 70)
    private String description;
}
