package com.quality.repository;

import com.quality.model.Priority;
import org.springframework.lang.NonNull;

public interface IPriorityRepository extends IGenericRepository<Priority, Integer> {
    
    /**
     * Check if a Priority with the given name exists.
     * @param name the name to check (must not be null)
     * @return true if exists, false otherwise
     */
    boolean existsByName(@NonNull String name);
    
    /**
     * Check if a Priority with the given name exists, excluding a specific ID.
     * Used for update operations to allow keeping the same name.
     * @param name the name to check (must not be null)
     * @param id the ID to exclude from the check (must not be null)
     * @return true if exists, false otherwise
     */
    boolean existsByNameAndIdPriorityNot(@NonNull String name, @NonNull Integer id);
}
