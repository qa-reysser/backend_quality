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
@Schema(description = "DTO de Moneda")
public class CurrencyDTO {
    
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idCurrency;

    @NotNull(message = "Code is required")
    @NotEmpty(message = "Code cannot be empty")
    @Size(min = 3, max = 3, message = "Code must be exactly 3 characters")
    @Schema(description = "Código ISO de la moneda (único)", example = "USD", minLength = 3, maxLength = 3)
    private String code;

    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Schema(description = "Nombre de la moneda", example = "Dólar Estadounidense", minLength = 3, maxLength = 50)
    private String name;

    @NotNull(message = "Symbol is required")
    @NotEmpty(message = "Symbol cannot be empty")
    @Size(min = 1, max = 5, message = "Symbol must be between 1 and 5 characters")
    @Schema(description = "Símbolo de la moneda", example = "$", minLength = 1, maxLength = 5)
    private String symbol;

    @NotNull(message = "Active status is required")
    @Schema(description = "Estado activo de la moneda", example = "true")
    private Boolean active;
}
