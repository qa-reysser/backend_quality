package com.quality.service.implement;

import com.quality.model.Currency;
import com.quality.repository.ICurrencyRepository;
import com.quality.repository.IGenericRepository;
import com.quality.service.ICurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Implementation of ICurrencyService.
 * Extends OperationsImplement to inherit standard CRUD operations.
 * Follows Dependency Inversion Principle (DIP) - depends on ICurrencyRepository abstraction.
 * Applies Single Responsibility Principle (SRP) - manages only Currency business logic.
 */
@Service
@RequiredArgsConstructor
public class CurrencyServiceImplement extends OperationsImplement<Currency, Integer> implements ICurrencyService {
    
    private final ICurrencyRepository repo;
    
    @Override
    @NonNull
    @SuppressWarnings("null")
    protected IGenericRepository<Currency, Integer> getRepo() {
        return repo;
    }
    
    @Override
    @NonNull
    protected String getResourceType() {
        return "Currency";
    }
}
