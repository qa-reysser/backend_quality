package com.quality.controller;

import com.quality.config.OpenApiHeaders;
import com.quality.dto.AccountDTO;
import com.quality.model.Account;
import com.quality.model.Client;
import com.quality.model.Currency;
import com.quality.model.TypeAccount;
import com.quality.service.implement.AccountServiceImplement;
import com.quality.service.implement.ClientServiceImplement;
import com.quality.service.implement.CurrencyServiceImplement;
import com.quality.service.implement.TypeAccountServiceImplement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Cuentas Bancarias", description = "Operaciones CRUD para la gestión de cuentas bancarias")
public class AccountController {
    private final AccountServiceImplement service;
    private final ClientServiceImplement clientService;
    private final TypeAccountServiceImplement typeAccountService;
    private final CurrencyServiceImplement currencyService;
    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    @OpenApiHeaders
    @Operation(
        summary = "Listar todas las cuentas", 
        description = "Obtiene la lista completa de cuentas bancarias registradas. " +
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
    public ResponseEntity<List<AccountDTO>> findAll() {
        List<AccountDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Obtener cuenta por ID", 
        description = "Recupera una cuenta bancaria específica mediante su identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cuenta encontrada exitosamente"),
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
            description = "Cuenta no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<AccountDTO> findById(
            @Parameter(description = "ID de la cuenta", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        Account obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @GetMapping("/by-account-number/{accountNumber}")
    @OpenApiHeaders
    @Operation(
        summary = "Obtener cuenta por número de cuenta", 
        description = "Recupera una cuenta bancaria específica mediante su número de cuenta."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cuenta encontrada exitosamente"),
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
            description = "Cuenta no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<AccountDTO> findByAccountNumber(
            @Parameter(description = "Número de cuenta", required = true, example = "1234567890123456")
            @PathVariable("accountNumber") @NonNull String accountNumber) {
        Account obj = service.findByAccountNumber(accountNumber);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    @OpenApiHeaders
    @Operation(
        summary = "Crear nueva cuenta", 
        description = "Crea una nueva cuenta bancaria en estado INACTIVE. " +
                     "El número de cuenta se genera automáticamente con formato profesional. " +
                     "NO enviar accountNumber en el request - se genera automáticamente. " +
                     "La cuenta debe ser activada posteriormente mediante el endpoint de activación."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente en estado INACTIVE. El accountNumber se genera automáticamente."),
        @ApiResponse(
            responseCode = "400",
            description = "Error de validación",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cliente, tipo de cuenta o moneda no encontrados",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    @SuppressWarnings("null")
    public ResponseEntity<Void> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la cuenta a crear", required = true)
            @Valid @RequestBody AccountDTO dto) {
        Account obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdAccount()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Actualizar cuenta", 
        description = "Actualiza completamente una cuenta existente por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Error de validación",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cuenta no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<AccountDTO> update(
            @Parameter(description = "ID de la cuenta a actualizar", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados de la cuenta", required = true)
            @Valid @RequestBody AccountDTO dto) {
        dto.setIdAccount(id);
        Account obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Eliminar cuenta", 
        description = "Elimina una cuenta específica por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
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
            description = "Cuenta no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la cuenta a eliminar", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    private AccountDTO convertToDto(@NonNull Account obj) {
        AccountDTO dto = mapper.map(obj, AccountDTO.class);
        dto.setIdClient(obj.getClient().getIdClient());
        dto.setIdTypeAccount(obj.getTypeAccount().getIdTypeAccount());
        dto.setIdCurrency(obj.getCurrency().getIdCurrency());
        return dto;
    }

    private Account convertToEntity(@NonNull AccountDTO dto) {
        Account account = new Account();
        
        // Map only the fields that should come from request
        account.setIdAccount(dto.getIdAccount());
        
        // Map balance only if provided, otherwise use entity default (BigDecimal.ZERO)
        if (dto.getBalance() != null) {
            account.setBalance(dto.getBalance());
        }
        
        // DO NOT map status - let entity use its default value (INACTIVE)
        // DO NOT map accountNumber - it will be generated in service layer
        
        // Set relationships
        Client client = clientService.findById(dto.getIdClient());
        TypeAccount typeAccount = typeAccountService.findById(dto.getIdTypeAccount());
        Currency currency = currencyService.findById(dto.getIdCurrency());
        
        account.setClient(client);
        account.setTypeAccount(typeAccount);
        account.setCurrency(currency);
        
        return account;
    }
}
