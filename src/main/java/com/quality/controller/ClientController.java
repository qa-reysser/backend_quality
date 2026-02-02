package com.quality.controller;

import com.quality.config.ErrorCodeDescriptions;
import com.quality.config.OpenApiHeaders;
import com.quality.dto.ClientDTO;
import com.quality.model.Client;
import com.quality.model.TypeDocument;
import com.quality.service.implement.ClientServiceImplement;
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
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones CRUD para la gestión de clientes")
public class ClientController {
    private final ClientServiceImplement service;
    private final TypeDocumentServiceImplement typeDocumentService;
    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @GetMapping
    @OpenApiHeaders
    @Operation(
        summary = "Listar todos los clientes", 
        description = "Obtiene la lista completa de clientes registrados. " +
                     "Requiere encabezados de validación (x-correlation-id, x-client-id, x-user-id)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(
            responseCode = "400", 
            description = ErrorCodeDescriptions.CLIENT_GET_ALL_400,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.HEADER_VALIDATION_ERROR
                )
            )
        )
    })
    public ResponseEntity<List<ClientDTO>> findAll() {
        List<ClientDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(list, OK);
    }

    @GetMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Obtener cliente por ID", 
        description = "Recupera un cliente específico mediante su identificador. " +
                     "Requiere encabezados de validación y que el ID exista en la base de datos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.CLIENT_GET_BY_ID_400,
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
            description = ErrorCodeDescriptions.CLIENT_GET_BY_ID_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.CLIENT_NOT_FOUND_ERROR
                )
            )
        )
    })
    public ResponseEntity<ClientDTO> findById(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        Client obj = service.findById(id);
        return new ResponseEntity<>(this.convertToDto(obj), OK);
    }

    @PostMapping
    @OpenApiHeaders
    @Operation(
        summary = "Crear nuevo cliente", 
        description = "Crea un nuevo cliente. El email y número de documento deben ser únicos y cumplir validaciones."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente. Header Location contiene la URI del nuevo recurso."),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.CLIENT_POST_400,
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
                        value = com.quality.config.SwaggerExamples.CLIENT_VALIDATION_ERROR
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = ErrorCodeDescriptions.CLIENT_POST_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "typeDocumentNotFound",
                    summary = "Tipo de documento no encontrado",
                    value = com.quality.config.SwaggerExamples.TYPE_DOCUMENT_NOT_FOUND_ERROR
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = ErrorCodeDescriptions.CLIENT_POST_409,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "duplicateEmail",
                        summary = "Email duplicado",
                        value = com.quality.config.SwaggerExamples.CLIENT_DUPLICATE_EMAIL_ERROR
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "duplicateDocument",
                        summary = "Número de documento duplicado",
                        value = com.quality.config.SwaggerExamples.CLIENT_DUPLICATE_DOCUMENT_ERROR
                    )
                }
            )
        )
    })
    @SuppressWarnings("null") // service.save() is @NonNull, guarantee satisfied
    public ResponseEntity<Void> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del cliente a crear (sin ID)", required = true)
            @Valid @RequestBody ClientDTO dto) {
        Client obj = service.save(convertToEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getIdClient()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Actualizar cliente", 
        description = "Actualiza un cliente existente. El ID debe existir, el email y número de documento deben permanecer únicos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.CLIENT_PUT_400,
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
                        value = com.quality.config.SwaggerExamples.CLIENT_VALIDATION_ERROR
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = ErrorCodeDescriptions.CLIENT_PUT_404_CLIENT + " | " + ErrorCodeDescriptions.CLIENT_PUT_404_TYPE_DOC,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "clientNotFound",
                        summary = "Cliente no encontrado",
                        value = com.quality.config.SwaggerExamples.CLIENT_NOT_FOUND_ERROR
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "typeDocumentNotFound",
                        summary = "Tipo de documento no encontrado",
                        value = com.quality.config.SwaggerExamples.TYPE_DOCUMENT_NOT_FOUND_ERROR
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = ErrorCodeDescriptions.CLIENT_PUT_409,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = {
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "duplicateEmail",
                        summary = "Email duplicado",
                        value = com.quality.config.SwaggerExamples.CLIENT_DUPLICATE_EMAIL_ERROR
                    ),
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        name = "duplicateDocument",
                        summary = "Número de documento duplicado",
                        value = com.quality.config.SwaggerExamples.CLIENT_DUPLICATE_DOCUMENT_ERROR
                    )
                }
            )
        )
    })
    @SuppressWarnings("null") // service.update() is @NonNull, guarantee satisfied
    public ResponseEntity<ClientDTO> update(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del cliente", required = true)
            @Valid @RequestBody ClientDTO dto) {
        dto.setIdClient(id);
        Client obj = service.update(convertToEntity(dto), id);
        return new ResponseEntity<>(convertToDto(obj), OK);
    }

    @DeleteMapping("/{id}")
    @OpenApiHeaders
    @Operation(
        summary = "Eliminar cliente", 
        description = "Elimina un cliente por su ID. Requiere que el ID exista en la base de datos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = ErrorCodeDescriptions.CLIENT_DELETE_400,
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
            description = ErrorCodeDescriptions.CLIENT_DELETE_404,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(ref = "#/components/schemas/ErrorResponse"),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = com.quality.config.SwaggerExamples.CLIENT_NOT_FOUND_ERROR
                )
            )
        )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable("id") @NonNull Integer id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @NonNull
    @SuppressWarnings("null") // ModelMapper guarantees non-null mapping result
    private ClientDTO convertToDto(@NonNull Client obj) {
        ClientDTO dto = Objects.requireNonNull(mapper.map(obj, ClientDTO.class), "Mapping result cannot be null");
        dto.setIdTypeDocument(obj.getTypeDocument().getIdTypeDocument());
        return dto;
    }

    @NonNull
    @SuppressWarnings("null") // ModelMapper guarantees non-null mapping result
    private Client convertToEntity(@NonNull ClientDTO dto) {
        Client client = Objects.requireNonNull(mapper.map(dto, Client.class), "Mapping result cannot be null");
        TypeDocument typeDocument = typeDocumentService.findById(dto.getIdTypeDocument());
        client.setTypeDocument(typeDocument);
        return client;
    }
}
