package com.quality.controller;

import com.quality.config.OpenApiHeaders;
import com.quality.dto.TypeAccountDTO;
import com.quality.model.TypeAccount;
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
@RequestMapping("/type-accounts")
@RequiredArgsConstructor
@Tag(name = "Tipos de Cuenta", description = "Operaciones CRUD para la gestión de tipos de cuenta bancaria")
public class TypeAccountController {
    private final TypeAccountServiceImplement service;
    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    @OpenApiHeaders
    @Operation(
        summary = "Listar todos los tipos de cuenta", 
        description = "Obtiene la lista completa de tipos de cuenta disponibles (Ahorro, Corriente, etc.). " +
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
    public ResponseEntity<List<TypeAccountDTO>> findAll() {
        List<TypeAccountDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Obtener tipo de cuenta por ID", 
        description = "Recupera un tipo de cuenta específico mediante su identificador."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de cuenta encontrado exitosamente"),
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
            description = "Tipo de cuenta no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<TypeAccountDTO> findById(
            @Parameter(description = "ID del tipo de cuenta", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        TypeAccount obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    @OpenApiHeaders
    @Operation(
        summary = "Crear nuevo tipo de cuenta", 
        description = "Crea un nuevo tipo de cuenta. El código debe ser único."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tipo de cuenta creado exitosamente"),
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
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del tipo de cuenta a crear", required = true)
            @Valid @RequestBody TypeAccountDTO dto) {
        TypeAccount obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdTypeAccount()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Actualizar tipo de cuenta", 
        description = "Actualiza completamente un tipo de cuenta existente por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de cuenta actualizado exitosamente"),
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
            description = "Tipo de cuenta no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<TypeAccountDTO> update(
            @Parameter(description = "ID del tipo de cuenta a actualizar", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del tipo de cuenta", required = true)
            @Valid @RequestBody TypeAccountDTO dto) {
        dto.setIdTypeAccount(id);
        TypeAccount obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Eliminar tipo de cuenta", 
        description = "Elimina un tipo de cuenta específico por su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tipo de cuenta eliminado exitosamente"),
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
            description = "Tipo de cuenta no encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse")
            )
        )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del tipo de cuenta a eliminar", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    private TypeAccountDTO convertToDto(@NonNull TypeAccount obj) {
        return mapper.map(obj, TypeAccountDTO.class);
    }

    private TypeAccount convertToEntity(@NonNull TypeAccountDTO dto) {
        return mapper.map(dto, TypeAccount.class);
    }
}
