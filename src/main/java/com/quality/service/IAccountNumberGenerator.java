package com.quality.service;

import com.quality.model.Currency;
import com.quality.model.TypeAccount;
import org.springframework.lang.NonNull;

/**
 * Service interface for generating unique account numbers.
 * Follows Single Responsibility Principle (SRP) - only handles account number generation.
 * Format: [TypeCode][CurrencyCode][Timestamp][Random][CheckDigit]
 */
public interface IAccountNumberGenerator {
    
    /**
     * Generates a unique account number based on account type and currency.
     * The generated number follows a standard banking format with check digit validation.
     * 
     * @param typeAccount the type of account (must not be null)
     * @param currency the currency of the account (must not be null)
     * @return a unique account number (never null)
     */
    @NonNull
    String generateAccountNumber(@NonNull TypeAccount typeAccount, @NonNull Currency currency);
    
    /**
     * Validates if an account number has a valid format and check digit.
     * 
     * @param accountNumber the account number to validate (must not be null)
     * @return true if valid, false otherwise
     */
    boolean isValidAccountNumber(@NonNull String accountNumber);
}
