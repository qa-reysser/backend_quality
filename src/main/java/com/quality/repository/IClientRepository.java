package com.quality.repository;

import com.quality.model.Client;
import org.springframework.lang.NonNull;

public interface IClientRepository extends IGenericRepository<Client, Integer> {
    
    /**
     * Check if a Client with the given email exists.
     * @param email the email to check (must not be null)
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(@NonNull String email);
    
    /**
     * Check if a Client with the given email exists, excluding a specific ID.
     * Used for update operations to allow keeping the same email.
     * @param email the email to check (must not be null)
     * @param id the ID to exclude from the check (must not be null)
     * @return true if exists, false otherwise
     */
    boolean existsByEmailAndIdClientNot(@NonNull String email, @NonNull Integer id);
    
    /**
     * Check if a Client with the given document number exists.
     * @param documentNumber the document number to check (must not be null)
     * @return true if exists, false otherwise
     */
    boolean existsByDocumentNumber(@NonNull String documentNumber);
    
    /**
     * Check if a Client with the given document number exists, excluding a specific ID.
     * Used for update operations to allow keeping the same document number.
     * @param documentNumber the document number to check (must not be null)
     * @param id the ID to exclude from the check (must not be null)
     * @return true if exists, false otherwise
     */
    boolean existsByDocumentNumberAndIdClientNot(@NonNull String documentNumber, @NonNull Integer id);
}
