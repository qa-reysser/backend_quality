package com.quality.dto;

import com.quality.model.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "DTO de Cuenta Bancaria")
public class AccountDTO {
    
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idAccount;

    @Schema(description = "Número de cuenta bancaria generado automáticamente (único)", example = "SAUSD17385212345678", accessMode = Schema.AccessMode.READ_ONLY)
    private String accountNumber;

    @NotNull(message = "Client ID is required")
    @Schema(description = "ID del cliente titular de la cuenta", example = "1")
    private Integer idClient;

    @NotNull(message = "Type account ID is required")
    @Schema(description = "ID del tipo de cuenta", example = "1")
    private Integer idTypeAccount;

    @NotNull(message = "Currency ID is required")
    @Schema(description = "ID de la moneda", example = "1")
    private Integer idCurrency;

    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be greater than or equal to 0")
    @Digits(integer = 13, fraction = 2, message = "Balance must have at most 13 integer digits and 2 decimal places")
    @Schema(description = "Saldo inicial de la cuenta (opcional, por defecto 0.00)", example = "1000.50")
    private BigDecimal balance;

    @Schema(description = "Estado de la cuenta (generado automáticamente como INACTIVE)", example = "INACTIVE", accessMode = Schema.AccessMode.READ_ONLY, allowableValues = {"INACTIVE", "ACTIVE", "BLOCKED"})
    private AccountStatus status;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdDate;

    @Schema(description = "Fecha de activación", example = "2024-01-16T14:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime activatedDate;
}
