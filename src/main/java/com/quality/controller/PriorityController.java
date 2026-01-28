package com.quality.controller;

import com.quality.config.ErrorCodeDescriptions;
import com.quality.config.OpenApiHeaders;
import com.quality.dto.PriorityDTO;
import com.quality.model.Priority;
import com.quality.service.implement.PriorityServiceImplement;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/priorities")
@RequiredArgsConstructor
@Tag(name = "Prioridades", description = "Operaciones CRUD para la gestión de prioridades")
public class PriorityController {
    private final PriorityServiceImplement service;
    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    @OpenApiHeaders
    @Operation(
        summary = "Listar todas las prioridades", 
        description = "Obtiene la lista completa de prioridades disponibles. " +
                     "Requiere encabezados de validación (x-correlation-id, x-client-id, x-user-id)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(
            responseCode = "400", 
            description = ErrorCodeDescriptions.GET_ALL_400,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.HEADER_VALIDATION_ERROR
                )
            )
        )
    })
    public ResponseEntity<List<PriorityDTO>> findAll() {
        List<PriorityDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Obtener prioridad por ID", 
        description = "Recupera una prioridad específica mediante su identificador. " +
                     "Requiere encabezados de validación y que el ID exista en la base de datos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prioridad encontrada exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.GET_BY_ID_400,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.HEADER_VALIDATION_ERROR
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = ErrorCodeDescriptions.GET_BY_ID_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.RESOURCE_NOT_FOUND_ERROR
                )
            )
        )
    })
    public ResponseEntity<PriorityDTO> findById(
            @Parameter(description = "ID de la prioridad", required = true, example = "1")
            @PathVariable("id") Integer id) {
        Priority obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    @OpenApiHeaders
    @Operation(
        summary = "Crear nueva prioridad", 
        description = "Crea una nueva prioridad. El nombre debe ser único y cumplir validaciones (3-70 caracteres)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Prioridad creada exitosamente. Header Location contiene la URI del nuevo recurso."),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.POST_400,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "headerError",
                        summary = "Error de encabezado",
                        value = com.quality.config.SwaggerExamples.HEADER_VALIDATION_ERROR
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "validationError",
                        summary = "Error de validación",
                        value = com.quality.config.SwaggerExamples.VALIDATION_ERROR
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = ErrorCodeDescriptions.POST_409,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.CONFLICT_ERROR
                )
            )
        )
    })
    public ResponseEntity<Void> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la prioridad a crear (sin ID)", required = true)
            @Valid @RequestBody PriorityDTO dto) {
        Priority obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdPriority()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Actualizar prioridad", 
        description = "Actualiza una prioridad existente. El ID debe existir y el nombre debe permanecer único."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prioridad actualizada exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.PUT_400,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "headerError",
                        summary = "Error de encabezado",
                        value = com.quality.config.SwaggerExamples.HEADER_VALIDATION_ERROR
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "validationError",
                        summary = "Error de validación",
                        value = com.quality.config.SwaggerExamples.VALIDATION_ERROR
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = ErrorCodeDescriptions.PUT_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.RESOURCE_NOT_FOUND_ERROR
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = ErrorCodeDescriptions.PUT_409,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.CONFLICT_ERROR
                )
            )
        )
    })
    public ResponseEntity<PriorityDTO> update(
            @Parameter(description = "ID de la prioridad", required = true, example = "1")
            @PathVariable("id") Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados de la prioridad", required = true)
            @Valid @RequestBody PriorityDTO dto) {
        dto.setIdPriority(id);
        Priority obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Eliminar prioridad", 
        description = "Elimina una prioridad por su ID. Requiere que el ID exista en la base de datos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Prioridad eliminada exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.DELETE_400,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.HEADER_VALIDATION_ERROR
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = ErrorCodeDescriptions.DELETE_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.RESOURCE_NOT_FOUND_ERROR
                )
            )
        )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la prioridad", required = true, example = "1")
            @PathVariable("id") Integer id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    private PriorityDTO convertToDto(Priority obj) {
        return mapper.map(obj, PriorityDTO.class);
    }

    private Priority convertToEntity(PriorityDTO dto) {
        return mapper.map(dto, Priority.class);
    }
}

