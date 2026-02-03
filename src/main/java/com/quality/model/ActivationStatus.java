package com.quality.model;

/**
 * Enumeration for account activation attempt statuses.
 * Tracks the result of activation verification processes.
 */
public enum ActivationStatus {
    /**
     * Activation successful.
     * All verification checks passed.
     */
    SUCCESS,
    
    /**
     * Activation failed.
     * Verification checks did not match.
     */
    FAILED
}
