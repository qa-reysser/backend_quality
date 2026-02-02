package com.quality.repository;

import com.quality.model.TypeDocument;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ITypeDocumentRepository extends IGenericRepository<TypeDocument, Integer> {
    
    /**
     * Check if a TypeDocument with the given code exists.
     * @param code the code to check (must not be null)
     * @return true if exists, false otherwise
     */
    boolean existsByCode(@NonNull String code);
    
    /**
     * Check if a TypeDocument with the given code exists, excluding a specific ID.
     * Used for update operations to allow keeping the same code.
     * @param code the code to check (must not be null)
     * @param id the ID to exclude from the check (must not be null)
     * @return true if exists, false otherwise
     */
    boolean existsByCodeAndIdTypeDocumentNot(@NonNull String code, @NonNull Integer id);
    
    /**
     * Find all active TypeDocuments.
     * @return list of active TypeDocuments (never null, may be empty)
     */
    @NonNull
    List<TypeDocument> findByActiveTrue();
}
