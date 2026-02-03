package com.quality.service.implement;

import com.quality.model.TypeAccount;
import com.quality.repository.IGenericRepository;
import com.quality.repository.ITypeAccountRepository;
import com.quality.service.ITypeAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Implementation of ITypeAccountService.
 * Extends OperationsImplement to inherit standard CRUD operations.
 * Follows Dependency Inversion Principle (DIP) - depends on ITypeAccountRepository abstraction.
 * Applies Single Responsibility Principle (SRP) - manages only TypeAccount business logic.
 */
@Service
@RequiredArgsConstructor
public class TypeAccountServiceImplement extends OperationsImplement<TypeAccount, Integer> implements ITypeAccountService {
    
    private final ITypeAccountRepository repo;
    
    @Override
    @NonNull
    @SuppressWarnings("null")
    protected IGenericRepository<TypeAccount, Integer> getRepo() {
        return repo;
    }
    
    @Override
    @NonNull
    protected String getResourceType() {
        return "TypeAccount";
    }
}
