package com.quality.service.implement;

import com.quality.exception.validation.DuplicateFieldException;
import com.quality.model.TypeDocument;
import com.quality.repository.IGenericRepository;
import com.quality.repository.ITypeDocumentRepository;
import com.quality.service.ITypeDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service implementation for TypeDocument operations.
 * Extends generic CRUD operations and adds business logic for duplicate code validation.
 * Follows Single Responsibility Principle - handles only TypeDocument-specific logic.
 */
@Service
@RequiredArgsConstructor
public class TypeDocumentServiceImplement extends OperationsImplement<TypeDocument, Integer> implements ITypeDocumentService {
    private final ITypeDocumentRepository repository;

    @Override
    @NonNull
    protected IGenericRepository<TypeDocument, Integer> getRepo() {
        return Objects.requireNonNull(repository, "Repository cannot be null");
    }

    @Override
    @NonNull
    protected String getResourceType() {
        return "TypeDocument";
    }

    @Override
    @NonNull
    public TypeDocument save(@NonNull TypeDocument typeDocument) {
        checkDuplicateCode(
            Objects.requireNonNull(typeDocument.getCode(), "TypeDocument code cannot be null"), 
            null
        );
        return super.save(typeDocument);
    }

    @Override
    @NonNull
    public TypeDocument update(@NonNull TypeDocument typeDocument, @NonNull Integer id) {
        checkDuplicateCode(
            Objects.requireNonNull(typeDocument.getCode(), "TypeDocument code cannot be null"), 
            id
        );
        return super.update(typeDocument, id);
    }

    /**
     * Check if a TypeDocument with the given code already exists.
     * Validates uniqueness constraint before save/update operations.
     * 
     * @param code the code to check (must not be null)
     * @param excludeId the ID to exclude from the check (null for save, id for update)
     * @throws DuplicateFieldException if a duplicate code is found
     */
    private void checkDuplicateCode(@NonNull String code, Integer excludeId) {
        boolean exists = (excludeId == null)
                ? repository.existsByCode(code)
                : repository.existsByCodeAndIdTypeDocumentNot(code, Objects.requireNonNull(excludeId, "Exclude ID cannot be null"));

        if (exists) {
            throw new DuplicateFieldException("code", code);
        }
    }
}
