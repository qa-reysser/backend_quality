package com.quality.service.implement;

import com.quality.model.Currency;
import com.quality.model.TypeAccount;
import com.quality.service.IAccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Objects;

/**
 * Implementation of IAccountNumberGenerator.
 * Generates unique account numbers following a professional banking format.
 * 
 * Format: [TypeCode(2)][CurrencyCode(3)][Timestamp(10)][Random(3)][CheckDigit(1)]
 * Total length: 19 characters
 * 
 * Example: SA-USD-1738521234-567-8
 * - SA: Savings Account type code
 * - USD: US Dollar currency code
 * - 1738521234: Timestamp (last 10 digits of epoch millis)
 * - 567: Random 3-digit number for uniqueness
 * - 8: Luhn check digit
 * 
 * Follows Single Responsibility Principle (SRP) - only handles account number generation.
 */
@Service
@RequiredArgsConstructor
public class AccountNumberGeneratorImplement implements IAccountNumberGenerator {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    @Override
    @NonNull
    public String generateAccountNumber(@NonNull TypeAccount typeAccount, @NonNull Currency currency) {
        Objects.requireNonNull(typeAccount, "TypeAccount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        Objects.requireNonNull(typeAccount.getCode(), "TypeAccount code cannot be null");
        Objects.requireNonNull(currency.getCode(), "Currency code cannot be null");
        
        // Extract type code (first 2 chars, uppercase)
        String typeCode = typeAccount.getCode().toUpperCase().substring(0, Math.min(2, typeAccount.getCode().length()));
        if (typeCode.length() < 2) {
            typeCode = String.format("%-2s", typeCode).replace(' ', '0');
        }
        
        // Extract currency code (3 chars, uppercase)
        String currencyCode = currency.getCode().toUpperCase();
        
        // Generate timestamp (last 10 digits of current time in millis)
        long timestamp = Instant.now().toEpochMilli();
        String timestampStr = String.valueOf(timestamp).substring(3); // Last 10 digits
        
        // Generate random 3-digit number for additional uniqueness
        int randomNum = RANDOM.nextInt(1000);
        String randomStr = String.format("%03d", randomNum);
        
        // Build account number without check digit
        String accountNumberBase = typeCode + currencyCode + timestampStr + randomStr;
        
        // Calculate and append Luhn check digit
        int checkDigit = calculateLuhnCheckDigit(accountNumberBase);
        
        return accountNumberBase + checkDigit;
    }
    
    @Override
    public boolean isValidAccountNumber(@NonNull String accountNumber) {
        Objects.requireNonNull(accountNumber, "Account number cannot be null");
        
        // Check minimum length (should be 19 characters)
        if (accountNumber.length() < 19) {
            return false;
        }
        
        // Extract base and check digit
        String base = accountNumber.substring(0, accountNumber.length() - 1);
        char checkDigitChar = accountNumber.charAt(accountNumber.length() - 1);
        
        // Validate check digit is a number
        if (!Character.isDigit(checkDigitChar)) {
            return false;
        }
        
        int providedCheckDigit = Character.getNumericValue(checkDigitChar);
        int calculatedCheckDigit = calculateLuhnCheckDigit(base);
        
        return providedCheckDigit == calculatedCheckDigit;
    }
    
    /**
     * Calculates Luhn check digit (mod 10 algorithm).
     * Industry standard for validating identification numbers.
     * Follows Single Responsibility Principle (SRP).
     * 
     * @param number the number to calculate check digit for
     * @return the check digit (0-9)
     */
    private int calculateLuhnCheckDigit(@NonNull String number) {
        int sum = 0;
        boolean alternate = false;
        
        // Process digits from right to left
        for (int i = number.length() - 1; i >= 0; i--) {
            char c = number.charAt(i);
            
            // Skip non-digits (in case of letters in type/currency codes)
            if (!Character.isDigit(c)) {
                // For letters, use their position value
                int letterValue = Character.toUpperCase(c) - 'A' + 1;
                int digit = letterValue % 10;
                
                if (alternate) {
                    digit *= 2;
                    if (digit > 9) {
                        digit -= 9;
                    }
                }
                sum += digit;
            } else {
                int digit = Character.getNumericValue(c);
                
                if (alternate) {
                    digit *= 2;
                    if (digit > 9) {
                        digit -= 9;
                    }
                }
                sum += digit;
            }
            
            alternate = !alternate;
        }
        
        return (10 - (sum % 10)) % 10;
    }
}
