package com.quality.service.implement;

import com.quality.exception.resource.ResourceNotFoundAfterOperationException;
import com.quality.exception.resource.ResourceNotFoundByIdException;
import com.quality.repository.IGenericRepository;
import com.quality.service.IOperations;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Abstract base class implementing generic CRUD operations.
 * Applies Template Method Pattern - defines the algorithm structure, 
 * delegates repository access to subclasses via getRepo().
 * Follows Open/Closed Principle (OCP) - open for extension, closed for modification.
 * 
 * @param <T> Entity type
 * @param <ID> ID type
 */
public abstract class OperationsImplement<T, ID> implements IOperations<T, ID> {
    
    /**
     * Template method for repository access.
     * Subclasses must override to provide the specific repository instance.
     * Applies Dependency Inversion Principle (DIP) - depends on IGenericRepository abstraction.
     * 
     * @return the repository instance (must not be null)
     */
    @NonNull
    protected abstract IGenericRepository<T, ID> getRepo();

    /**
     * Gets the resource type name for error messages.
     * Override this in subclasses to provide specific resource names.
     */
    @NonNull
    protected String getResourceType() {
        return "Resource";
    }

    @Override
    @NonNull
    public T save(@NonNull T t) {
        IGenericRepository<T, ID> repo = Objects.requireNonNull(getRepo(), "Repository cannot be null");
        return repo.save(t);
    }

    @Override
    @NonNull
    public T update(@NonNull T t, @NonNull ID id) {
        IGenericRepository<T, ID> repo = Objects.requireNonNull(getRepo(), "Repository cannot be null");
        repo.findById(id).orElseThrow(() -> 
            new ResourceNotFoundAfterOperationException(getResourceType(), id, "update"));
        return repo.save(t);
    }

    @Override
    @NonNull
    public List<T> findAll() {
        IGenericRepository<T, ID> repo = Objects.requireNonNull(getRepo(), "Repository cannot be null");
        return repo.findAll();
    }

    /**
     * Finds an entity by ID.
     * @throws ResourceNotFoundByIdException if entity is not found
     */
    @Override
    @NonNull
    @SuppressWarnings("null") // JPA Optional.orElseThrow guarantees non-null return
    public T findById(@NonNull ID id) {
        IGenericRepository<T, ID> repo = Objects.requireNonNull(getRepo(), "Repository cannot be null");
        return repo.findById(id).orElseThrow(() -> 
            new ResourceNotFoundByIdException(getResourceType(), id));
    }

    @Override
    public void delete(@NonNull ID id) {
        IGenericRepository<T, ID> repo = Objects.requireNonNull(getRepo(), "Repository cannot be null");
        repo.findById(id).orElseThrow(() -> 
            new ResourceNotFoundAfterOperationException(getResourceType(), id, "delete"));
        repo.deleteById(id);
    }
}
