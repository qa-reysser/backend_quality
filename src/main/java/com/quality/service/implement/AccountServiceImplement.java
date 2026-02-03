package com.quality.service.implement;

import com.quality.exception.resource.ResourceNotFoundByIdException;
import com.quality.model.Account;
import com.quality.repository.IAccountRepository;
import com.quality.repository.IGenericRepository;
import com.quality.service.IAccountNumberGenerator;
import com.quality.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Implementation of IAccountService.
 * Extends OperationsImplement to inherit standard CRUD operations.
 * Follows Dependency Inversion Principle (DIP) - depends on IAccountRepository abstraction.
 * Applies Single Responsibility Principle (SRP) - manages only Account business logic.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImplement extends OperationsImplement<Account, Integer> implements IAccountService {
    
    private final IAccountRepository repo;
    private final IAccountNumberGenerator accountNumberGenerator;
    
    @Override
    @NonNull
    @SuppressWarnings("null")
    protected IGenericRepository<Account, Integer> getRepo() {
        return repo;
    }
    
    @Override
    @NonNull
    protected String getResourceType() {
        return "Account";
    }
    
    @Override
    @NonNull
    public Account save(@NonNull Account account) {
        Objects.requireNonNull(account, "Account cannot be null");
        
        // Generate account number automatically
        String generatedAccountNumber = accountNumberGenerator.generateAccountNumber(
                account.getTypeAccount(),
                account.getCurrency()
        );
        account.setAccountNumber(generatedAccountNumber);
        
        // Save account
        return super.save(account);
    }
    
    @Override
    @NonNull
    @SuppressWarnings("null")
    public Account findByAccountNumber(@NonNull String accountNumber) {
        Objects.requireNonNull(accountNumber, "Account number cannot be null");
        return repo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundByIdException(
                        "Account", 
                        accountNumber
                ));
    }
}
