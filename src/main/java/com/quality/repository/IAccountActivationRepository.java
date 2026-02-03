package com.quality.repository;

import com.quality.model.AccountActivation;

/**
 * Repository interface for AccountActivation entity.
 * Extends IGenericRepository to inherit standard CRUD operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface IAccountActivationRepository extends IGenericRepository<AccountActivation, Integer> {
}
