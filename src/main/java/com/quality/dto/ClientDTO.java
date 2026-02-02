package com.quality.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "DTO de Cliente")
public class ClientDTO {
    
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idClient;

    @NotNull(message = "First name is required")
    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "Nombre del cliente", example = "Juan", minLength = 2, maxLength = 50)
    private String firstName;

    @NotNull(message = "Last name is required")
    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "Apellido del cliente", example = "Pérez", minLength = 2, maxLength = 50)
    private String lastName;

    @NotNull(message = "Type document ID is required")
    @Schema(description = "ID del tipo de documento", example = "1")
    private Integer idTypeDocument;

    @NotNull(message = "Document number is required")
    @NotEmpty(message = "Document number cannot be empty")
    @Size(min = 3, max = 20, message = "Document number must be between 3 and 20 characters")
    @Schema(description = "Número de documento (único)", example = "12345678", minLength = 3, maxLength = 20)
    private String documentNumber;

    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Correo electrónico del cliente (único)", example = "juan.perez@example.com", maxLength = 100)
    private String email;

    @NotNull(message = "Phone is required")
    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]+$", message = "Phone must contain only valid characters")
    @Size(min = 7, max = 20, message = "Phone must be between 7 and 20 characters")
    @Schema(description = "Número de teléfono del cliente", example = "+51987654321", minLength = 7, maxLength = 20)
    private String phone;
}
