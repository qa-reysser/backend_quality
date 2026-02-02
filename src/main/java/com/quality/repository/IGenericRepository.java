package com.quality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface extending JpaRepository.
 * Adds @NonNull annotations to key methods for null safety compliance.
 * Follows Interface Segregation Principle (ISP).
 * 
 * @param <T> Entity type
 * @param <ID> ID type
 */
@NoRepositoryBean
public interface IGenericRepository<T, ID> extends JpaRepository<T, ID> {
    
    /**
     * Saves a given entity.
     * @param entity entity to save (must not be null)
     * @return the saved entity (never null)
     */
    @Override
    @NonNull
    <S extends T> S save(@NonNull S entity);
    
    /**
     * Retrieves all entities.
     * @return list of all entities (never null, may be empty)
     */
    @Override
    @NonNull
    List<T> findAll();
    
    /**
     * Retrieves an entity by its id.
     * @param id must not be null
     * @return the entity with the given id or Optional#empty() if none found (never null)
     */
    @Override
    @NonNull
    Optional<T> findById(@NonNull ID id);
    
    /**
     * Deletes the entity with the given id.
     * @param id must not be null
     */
    @Override
    void deleteById(@NonNull ID id);
}
