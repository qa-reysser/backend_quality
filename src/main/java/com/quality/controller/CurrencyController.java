package com.quality.controller;

import com.quality.config.OpenApiHeaders;
import com.quality.dto.CurrencyDTO;
import com.quality.model.Currency;
import com.quality.service.implement.CurrencyServiceImplement;
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
@RequestMapping("/currencies")
@RequiredArgsConstructor
@Tag(name = "Monedas", description = "Operaciones CRUD para la gestión de monedas")
public class CurrencyController {
    private final CurrencyServiceImplement service;
    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    @OpenApiHeaders
    @Operation(
        summary = "Listar todas las monedas", 
        description = "Obtiene la lista completa de monedas disponibles (USD, PEN, EUR, etc.). " +
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
    public ResponseEntity<List<CurrencyDTO>> findAll() {
        List<CurrencyDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Obtener moneda por ID", 
        description = "Recupera una moneda específica mediante su identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Moneda encontrada exitosamente"),
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
            description = "Moneda no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<CurrencyDTO> findById(
            @Parameter(description = "ID de la moneda", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        Currency obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    @OpenApiHeaders
    @Operation(
        summary = "Crear nueva moneda", 
        description = "Crea una nueva moneda. El código debe ser único y tener exactamente 3 caracteres (ISO 4217)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Moneda creada exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Error de validación",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    @SuppressWarnings("null")
    public ResponseEntity<Void> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la moneda a crear", required = true)
            @Valid @RequestBody CurrencyDTO dto) {
        Currency obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdCurrency()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @OpenApiHeaders
    @SuppressWarnings("null")
    @Operation(
        summary = "Actualizar moneda", 
        description = "Actualiza completamente una moneda existente por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Moneda actualizada exitosamente"),
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
            description = "Moneda no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<CurrencyDTO> update(
            @Parameter(description = "ID de la moneda a actualizar", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados de la moneda", required = true)
            @Valid @RequestBody CurrencyDTO dto) {
        dto.setIdCurrency(id);
        Currency obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Eliminar moneda", 
        description = "Elimina una moneda específica por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Moneda eliminada exitosamente"),
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
            description = "Moneda no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la moneda a eliminar", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    private CurrencyDTO convertToDto(@NonNull Currency obj) {
        return mapper.map(obj, CurrencyDTO.class);
    }

    private Currency convertToEntity(@NonNull CurrencyDTO dto) {
        return mapper.map(dto, Currency.class);
    }
}
