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
@Schema(description = "DTO de Tipo de Cuenta")
public class TypeAccountDTO {
    
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idTypeAccount;

    @NotNull(message = "Code is required")
    @NotEmpty(message = "Code cannot be empty")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    @Schema(description = "Código del tipo de cuenta (único)", example = "SAV", minLength = 2, maxLength = 20)
    private String code;

    @NotNull(message = "Description is required")
    @NotEmpty(message = "Description cannot be empty")
    @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")
    @Schema(description = "Descripción del tipo de cuenta", example = "Cuenta de Ahorros", minLength = 3, maxLength = 100)
    private String description;

    @NotNull(message = "Active status is required")
    @Schema(description = "Estado activo del tipo de cuenta", example = "true")
    private Boolean active;
}
