package com.quality.service;

import com.quality.model.Account;
import org.springframework.lang.NonNull;

/**
 * Service interface for Account operations.
 * Extends IOperations to inherit standard CRUD operations.
 * Adds custom methods for account-specific business logic.
 * Follows Interface Segregation Principle (ISP).
 */
public interface IAccountService extends IOperations<Account, Integer> {
    
    /**
     * Finds an account by its account number.
     * @param accountNumber the account number to search for (must not be null)
     * @return the found account (never null)
     * @throws com.quality.exception.resource.ResourceNotFoundByIdException if not found
     */
    @NonNull
    Account findByAccountNumber(@NonNull String accountNumber);
}
