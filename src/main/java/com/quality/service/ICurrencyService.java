package com.quality.service;

import com.quality.model.Currency;

/**
 * Service interface for Currency operations.
 * Extends IOperations to inherit standard CRUD operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface ICurrencyService extends IOperations<Currency, Integer> {
}
