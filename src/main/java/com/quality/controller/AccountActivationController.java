package com.quality.controller;

import com.quality.config.OpenApiHeaders;
import com.quality.dto.AccountActivationDTO;
import com.quality.model.AccountActivation;
import com.quality.service.implement.AccountActivationServiceImplement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/account-activations")
@RequiredArgsConstructor
@Tag(name = "Activación de Cuentas", description = "Operaciones para activar cuentas bancarias mediante validación de identidad")
public class AccountActivationController {
    private final AccountActivationServiceImplement service;

    @GetMapping
    @OpenApiHeaders
    @Operation(
        summary = "Listar todos los intentos de activación", 
        description = "Obtiene el historial completo de intentos de activación (exitosos y fallidos). " +
                     "Requiere encabezados de validación (x-correlation-id, x-client-id, x-user-id)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(
            responseCode = "400", 
            description = "Error de validación de encabezados",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<List<AccountActivationDTO>> findAll() {
        List<AccountActivationDTO> list = service.findAll().stream().map(this::convertToSafeDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Obtener activación por ID", 
        description = "Recupera un registro de activación específico mediante su identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro de activación encontrado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Error de validación de encabezados",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Registro de activación no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<AccountActivationDTO> findById(
            @Parameter(description = "ID del registro de activación", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        AccountActivation obj = service.findById(id);
        return new ResponseEntity<>(this.convertToSafeDto(obj), OK);
    }

    @PostMapping("/activate")
    @OpenApiHeaders
    @Operation(
        summary = "Activar cuenta bancaria", 
        description = "Procesa una solicitud de activación de cuenta. " +
                     "Valida que el tipo de documento y número de documento proporcionados coincidan con el titular de la cuenta. " +
                     "Si la validación es exitosa, la cuenta cambia a estado ACTIVE. " +
                     "Si falla, se registra el intento con la razón del error. " +
                     "Todos los intentos quedan registrados para auditoría."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Solicitud de activación procesada. " +
                         "Verificar campo 'activationStatus' en la respuesta: SUCCESS si se activó, FAILED si no coinciden los datos."
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error de validación de datos de entrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cuenta o tipo de documento no encontrados",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    @SuppressWarnings("null")
    public ResponseEntity<AccountActivationDTO> activateAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de activación: número de cuenta, tipo de documento y número de documento del titular", 
                required = true
            )
            @Valid @RequestBody AccountActivationDTO dto) {
        AccountActivation activation = service.activateAccount(dto);
        AccountActivationDTO responseDto = convertToActivationResponseDto(activation);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/account-activations/{id}")
                .buildAndExpand(activation.getIdAccountActivation())
                .toUri();
        
        return ResponseEntity.created(location).body(responseDto);
    }

    @DeleteMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Eliminar registro de activación", 
        description = "Elimina un registro de activación específico por su ID. " +
                     "Nota: Esto no desactiva la cuenta, solo elimina el registro de auditoría."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro eliminado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Error de validación de encabezados",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Registro de activación no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del registro de activación a eliminar", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * Converts to safe DTO without exposing sensitive information.
     * Hides: document numbers, account numbers, type document codes.
     * Shows only: ID, account ID reference, status, error reason, date.
     */
    private AccountActivationDTO convertToSafeDto(@NonNull AccountActivation obj) {
        AccountActivationDTO dto = new AccountActivationDTO();
        dto.setIdAccountActivation(obj.getIdAccountActivation());
        dto.setIdAccount(obj.getAccount().getIdAccount());
        dto.setActivationStatus(obj.getActivationStatus());
        dto.setErrorReason(obj.getErrorReason());
        dto.setAttemptDate(obj.getAttemptDate());
        return dto;
    }

    /**
     * Converts to minimal DTO for activation response - only the result.
     * Returns only: activationStatus, errorReason (if failed).
     */
    private AccountActivationDTO convertToActivationResponseDto(@NonNull AccountActivation obj) {
        AccountActivationDTO dto = new AccountActivationDTO();
        dto.setActivationStatus(obj.getActivationStatus());
        dto.setErrorReason(obj.getErrorReason());
        return dto;
    }
}
