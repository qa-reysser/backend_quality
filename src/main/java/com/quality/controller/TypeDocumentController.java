package com.quality.controller;

import com.quality.config.ErrorCodeDescriptions;
import com.quality.config.OpenApiHeaders;
import com.quality.dto.TypeDocumentDTO;
import com.quality.model.TypeDocument;
import com.quality.service.implement.TypeDocumentServiceImplement;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/type-documents")
@RequiredArgsConstructor
@Tag(name = "Tipos de Documento", description = "Operaciones CRUD para la gestión de tipos de documento")
public class TypeDocumentController {
    private final TypeDocumentServiceImplement service;
    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    @OpenApiHeaders
    @Operation(
        summary = "Listar todos los tipos de documento", 
        description = "Obtiene la lista completa de tipos de documento disponibles. " +
                     "Requiere encabezados de validación (x-correlation-id, x-client-id, x-user-id)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(
            responseCode = "400", 
            description = ErrorCodeDescriptions.TYPE_DOC_GET_ALL_400,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.HEADER_VALIDATION_ERROR
                )
            )
        )
    })
    public ResponseEntity<List<TypeDocumentDTO>> findAll() {
        List<TypeDocumentDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Obtener tipo de documento por ID", 
        description = "Recupera un tipo de documento específico mediante su identificador. " +
                     "Requiere encabezados de validación y que el ID exista en la base de datos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de documento encontrado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.TYPE_DOC_GET_BY_ID_400,
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
            description = ErrorCodeDescriptions.TYPE_DOC_GET_BY_ID_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.TYPE_DOCUMENT_NOT_FOUND_ERROR
                )
            )
        )
    })
    public ResponseEntity<TypeDocumentDTO> findById(
            @Parameter(description = "ID del tipo de documento", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        TypeDocument obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    @OpenApiHeaders
    @Operation(
        summary = "Crear nuevo tipo de documento", 
        description = "Crea un nuevo tipo de documento. El código debe ser único y cumplir validaciones (2-20 caracteres)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tipo de documento creado exitosamente. Header Location contiene la URI del nuevo recurso."),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.TYPE_DOC_POST_400,
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
            description = ErrorCodeDescriptions.TYPE_DOC_POST_409,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.TYPE_DOCUMENT_DUPLICATE_CODE_ERROR
                )
            )
        )
    })
    @SuppressWarnings("null") // service.save() is @NonNull, guarantee satisfied
    public ResponseEntity<Void> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del tipo de documento a crear (sin ID)", required = true)
            @Valid @RequestBody TypeDocumentDTO dto) {
        TypeDocument obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdTypeDocument()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Actualizar tipo de documento", 
        description = "Actualiza un tipo de documento existente. El ID debe existir y el código debe permanecer único."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tipo de documento actualizado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.TYPE_DOC_PUT_400,
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
            description = ErrorCodeDescriptions.TYPE_DOC_PUT_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.TYPE_DOCUMENT_NOT_FOUND_ERROR
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = ErrorCodeDescriptions.TYPE_DOC_PUT_409,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.TYPE_DOCUMENT_DUPLICATE_CODE_ERROR
                )
            )
        )
    })
    @SuppressWarnings("null") // service.update() is @NonNull, guarantee satisfied
    public ResponseEntity<TypeDocumentDTO> update(
            @Parameter(description = "ID del tipo de documento", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del tipo de documento", required = true)
            @Valid @RequestBody TypeDocumentDTO dto) {
        dto.setIdTypeDocument(id);
        TypeDocument obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Eliminar tipo de documento", 
        description = "Elimina un tipo de documento por su ID. Requiere que el ID exista en la base de datos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Tipo de documento eliminado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.TYPE_DOC_DELETE_400,
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
            description = ErrorCodeDescriptions.TYPE_DOC_DELETE_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.TYPE_DOCUMENT_NOT_FOUND_ERROR
                )
            )
        )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del tipo de documento", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @NonNull
    @SuppressWarnings("null") // ModelMapper guarantees non-null mapping result
    private TypeDocumentDTO convertToDto(@NonNull TypeDocument obj) {
        return Objects.requireNonNull(mapper.map(obj, TypeDocumentDTO.class), "Mapping result cannot be null");
    }

    @NonNull
    @SuppressWarnings("null") // ModelMapper guarantees non-null mapping result
    private TypeDocument convertToEntity(@NonNull TypeDocumentDTO dto) {
        return Objects.requireNonNull(mapper.map(dto, TypeDocument.class), "Mapping result cannot be null");
    }
}
