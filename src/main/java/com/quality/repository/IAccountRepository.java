package com.quality.repository;

import com.quality.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repository interface for Account entity.
 * Extends IGenericRepository to inherit standard CRUD operations.
 * Adds custom queries for account-specific operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface IAccountRepository extends IGenericRepository<Account, Integer> {
    
    /**
     * Finds an account by its account number.
     * @param accountNumber the account number to search for (must not be null)
     * @return Optional containing the account if found, empty otherwise (never null)
     */
    @NonNull
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(@NonNull @Param("accountNumber") String accountNumber);
}
