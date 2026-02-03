package com.quality.service;

import com.quality.model.TypeAccount;

/**
 * Service interface for TypeAccount operations.
 * Extends IOperations to inherit standard CRUD operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface ITypeAccountService extends IOperations<TypeAccount, Integer> {
}
