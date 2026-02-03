package com.quality.dto;

import com.quality.model.ActivationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "DTO de Activación de Cuenta")
public class AccountActivationDTO {
    
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idAccountActivation;

    @Schema(description = "ID de la cuenta a activar", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idAccount;

    @NotNull(message = "Type document ID is required")
    @Schema(description = "ID del tipo de documento proporcionado", example = "1")
    private Integer idTypeDocumentProvided;

    @NotNull(message = "Document number is required")
    @NotEmpty(message = "Document number cannot be empty")
    @Size(min = 3, max = 20, message = "Document number must be between 3 and 20 characters")
    @Schema(description = "Número de documento proporcionado", example = "12345678", minLength = 3, maxLength = 20)
    private String documentNumberProvided;

    @NotNull(message = "Account number is required")
    @NotEmpty(message = "Account number cannot be empty")
    @Size(min = 10, max = 20, message = "Account number must be between 10 and 20 characters")
    @Schema(description = "Número de cuenta proporcionado", example = "1234567890123456", minLength = 10, maxLength = 20)
    private String accountNumberProvided;

    @Schema(description = "Estado de la activación", example = "SUCCESS", accessMode = Schema.AccessMode.READ_ONLY, allowableValues = {"SUCCESS", "FAILED"})
    private ActivationStatus activationStatus;

    @Schema(description = "Razón del error si la activación falló", example = "Document number does not match", accessMode = Schema.AccessMode.READ_ONLY)
    private String errorReason;

    @Schema(description = "Fecha del intento de activación", example = "2024-01-16T14:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime attemptDate;
}
