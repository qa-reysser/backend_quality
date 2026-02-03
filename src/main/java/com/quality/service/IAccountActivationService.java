package com.quality.service;

import com.quality.dto.AccountActivationDTO;
import com.quality.model.AccountActivation;
import org.springframework.lang.NonNull;

/**
 * Service interface for AccountActivation operations.
 * Extends IOperations to inherit standard CRUD operations.
 * Adds custom methods for account activation business logic.
 * Follows Interface Segregation Principle (ISP).
 */
public interface IAccountActivationService extends IOperations<AccountActivation, Integer> {
    
    /**
     * Processes an account activation request.
     * Validates the provided document information against the account owner.
     * If validation passes, activates the account and records the successful activation.
     * If validation fails, records the failed attempt with error reason.
     * 
     * @param dto the activation request with document and account information (must not be null)
     * @return the activation record with status and optional error reason (never null)
     * @throws com.quality.exception.resource.ResourceNotFoundByIdException if account not found
     */
    @NonNull
    AccountActivation activateAccount(@NonNull AccountActivationDTO dto);
}
