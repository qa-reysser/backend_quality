package com.quality.service.implement;

import com.quality.exception.validation.DuplicateFieldException;
import com.quality.model.Client;
import com.quality.repository.IClientRepository;
import com.quality.repository.IGenericRepository;
import com.quality.repository.ITypeDocumentRepository;
import com.quality.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service implementation for Client operations.
 * Extends generic CRUD operations and adds business logic for duplicate email and document validation.
 * Follows Single Responsibility Principle - handles only Client-specific logic.
 */
@Service
@RequiredArgsConstructor
public class ClientServiceImplement extends OperationsImplement<Client, Integer> implements IClientService {
    private final IClientRepository repository;
    private final ITypeDocumentRepository typeDocumentRepository;

    @Override
    @NonNull
    protected IGenericRepository<Client, Integer> getRepo() {
        return Objects.requireNonNull(repository, "Repository cannot be null");
    }

    @Override
    @NonNull
    protected String getResourceType() {
        return "Client";
    }

    @Override
    @NonNull
    public Client save(@NonNull Client client) {
        validateTypeDocumentExists(client.getTypeDocument().getIdTypeDocument());
        checkDuplicateEmail(
            Objects.requireNonNull(client.getEmail(), "Client email cannot be null"), 
            null
        );
        checkDuplicateDocumentNumber(
            Objects.requireNonNull(client.getDocumentNumber(), "Client document number cannot be null"), 
            null
        );
        return super.save(client);
    }

    @Override
    @NonNull
    public Client update(@NonNull Client client, @NonNull Integer id) {
        validateTypeDocumentExists(client.getTypeDocument().getIdTypeDocument());
        checkDuplicateEmail(
            Objects.requireNonNull(client.getEmail(), "Client email cannot be null"), 
            id
        );
        checkDuplicateDocumentNumber(
            Objects.requireNonNull(client.getDocumentNumber(), "Client document number cannot be null"), 
            id
        );
        return super.update(client, id);
    }

    /**
     * Validates that the referenced TypeDocument exists.
     * 
     * @param typeDocumentId the ID of the TypeDocument to validate
     * @throws com.quality.exception.resource.ResourceNotFoundByIdException if TypeDocument doesn't exist
     */
    private void validateTypeDocumentExists(Integer typeDocumentId) {
        Objects.requireNonNull(typeDocumentId, "TypeDocument ID cannot be null");
        typeDocumentRepository.findById(typeDocumentId)
            .orElseThrow(() -> new com.quality.exception.resource.ResourceNotFoundByIdException("TypeDocument", typeDocumentId));
    }

    /**
     * Check if a Client with the given email already exists.
     * Validates uniqueness constraint before save/update operations.
     * 
     * @param email the email to check (must not be null)
     * @param excludeId the ID to exclude from the check (null for save, id for update)
     * @throws DuplicateFieldException if a duplicate email is found
     */
    private void checkDuplicateEmail(@NonNull String email, Integer excludeId) {
        boolean exists = (excludeId == null)
                ? repository.existsByEmail(email)
                : repository.existsByEmailAndIdClientNot(email, Objects.requireNonNull(excludeId, "Exclude ID cannot be null"));

        if (exists) {
            throw new DuplicateFieldException("email", email);
        }
    }

    /**
     * Check if a Client with the given document number already exists.
     * Validates uniqueness constraint before save/update operations.
     * 
     * @param documentNumber the document number to check (must not be null)
     * @param excludeId the ID to exclude from the check (null for save, id for update)
     * @throws DuplicateFieldException if a duplicate document number is found
     */
    private void checkDuplicateDocumentNumber(@NonNull String documentNumber, Integer excludeId) {
        boolean exists = (excludeId == null)
                ? repository.existsByDocumentNumber(documentNumber)
                : repository.existsByDocumentNumberAndIdClientNot(documentNumber, Objects.requireNonNull(excludeId, "Exclude ID cannot be null"));

        if (exists) {
            throw new DuplicateFieldException("documentNumber", documentNumber);
        }
    }
}
