package com.quality.service;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Generic CRUD operations interface.
 * Defines the contract for basic Create, Read, Update, Delete operations.
 * Follows Interface Segregation Principle (ISP) - provides only essential CRUD methods.
 * 
 * @param <T> Entity type
 * @param <ID> ID type
 */
public interface IOperations<T, ID> {
    
    /**
     * Saves a new entity.
     * @param t entity to save (must not be null)
     * @return saved entity with generated ID
     */
    @NonNull
    T save(@NonNull T t);

    /**
     * Updates an existing entity.
     * @param t entity with new data (must not be null)
     * @param id ID of entity to update (must not be null)
     * @return updated entity
     */
    @NonNull
    T update(@NonNull T t, @NonNull ID id);

    /**
     * Retrieves all entities.
     * @return list of all entities (never null, may be empty)
     */
    @NonNull
    List<T> findAll();

    /**
     * Finds an entity by ID.
     * @param id ID to search for (must not be null)
     * @return found entity
     * @throws com.quality.exception.resource.ResourceNotFoundByIdException if not found
     */
    @NonNull
    T findById(@NonNull ID id);

    /**
     * Deletes an entity by ID.
     * @param id ID of entity to delete (must not be null)
     * @throws com.quality.exception.resource.ResourceNotFoundAfterOperationException if not found
     */
    void delete(@NonNull ID id);
}
