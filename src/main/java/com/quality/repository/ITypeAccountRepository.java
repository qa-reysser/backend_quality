package com.quality.repository;

import com.quality.model.TypeAccount;

/**
 * Repository interface for TypeAccount entity.
 * Extends IGenericRepository to inherit standard CRUD operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface ITypeAccountRepository extends IGenericRepository<TypeAccount, Integer> {
}
