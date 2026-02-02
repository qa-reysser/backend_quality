package com.quality.service.implement;

import com.quality.exception.validation.DuplicateFieldException;
import com.quality.model.Priority;
import com.quality.repository.IGenericRepository;
import com.quality.repository.IPriorityRepository;
import com.quality.service.IPriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service implementation for Priority operations.
 * Extends generic CRUD operations and adds business logic for duplicate name validation.
 * Follows Single Responsibility Principle - handles only Priority-specific logic.
 */
@Service
@RequiredArgsConstructor
public class PriorityServiceImplement extends OperationsImplement<Priority, Integer> implements IPriorityService {
    private final IPriorityRepository repository;

    @Override
    @NonNull
    protected IGenericRepository<Priority, Integer> getRepo() {
        return Objects.requireNonNull(repository, "Repository cannot be null");
    }

    @Override
    @NonNull
    protected String getResourceType() {
        return "Priority";
    }

    @Override
    @NonNull
    public Priority save(@NonNull Priority priority) {
        checkDuplicateName(
            Objects.requireNonNull(priority.getName(), "Priority name cannot be null"), 
            null
        );
        return super.save(priority);
    }

    @Override
    @NonNull
    public Priority update(@NonNull Priority priority, @NonNull Integer id) {
        checkDuplicateName(
            Objects.requireNonNull(priority.getName(), "Priority name cannot be null"), 
            id
        );
        return super.update(priority, id);
    }

    /**
     * Check if a Priority with the given name already exists.
     * Validates uniqueness constraint before save/update operations.
     * 
     * @param name the name to check (must not be null)
     * @param excludeId the ID to exclude from the check (null for save, id for update)
     * @throws DuplicateFieldException if a duplicate name is found
     */
    private void checkDuplicateName(@NonNull String name, Integer excludeId) {
        boolean exists = (excludeId == null)
                ? repository.existsByName(name)
                : repository.existsByNameAndIdPriorityNot(name, Objects.requireNonNull(excludeId, "Exclude ID cannot be null"));

        if (exists) {
            throw new DuplicateFieldException("name", name);
        }
    }
}
