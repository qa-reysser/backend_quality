package com.quality.model;

/**
 * Enumeration for account statuses.
 * Defines the lifecycle states of a bank account.
 */
public enum AccountStatus {
    /**
     * Account created but not yet activated.
     * Cannot perform transactions.
     */
    INACTIVE,
    
    /**
     * Account activated and operational.
     * Can perform all transactions.
     */
    ACTIVE,
    
    /**
     * Account temporarily or permanently blocked.
     * Cannot perform transactions.
     */
    BLOCKED
}
