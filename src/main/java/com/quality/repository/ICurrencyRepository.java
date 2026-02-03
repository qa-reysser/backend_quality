package com.quality.repository;

import com.quality.model.Currency;

/**
 * Repository interface for Currency entity.
 * Extends IGenericRepository to inherit standard CRUD operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface ICurrencyRepository extends IGenericRepository<Currency, Integer> {
}
