package com.quality.repository;

import com.quality.model.Priority;

public interface IPriorityRepository extends IGenericRepository<Priority, Integer> {
    
    /**
     * Check if a Priority with the given name exists.
     * @param name the name to check
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
    
    /**
     * Check if a Priority with the given name exists, excluding a specific ID.
     * Used for update operations to allow keeping the same name.
     * @param name the name to check
     * @param id the ID to exclude from the check
     * @return true if exists, false otherwise
     */
    boolean existsByNameAndIdPriorityNot(String name, Integer id);
}
