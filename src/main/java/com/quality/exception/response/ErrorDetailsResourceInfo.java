package com.quality.exception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Detailed information about resource not found errors.
 * Used for 404 errors to provide context about the missing resource.
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorDetailsResourceInfo {
    private String resourceType;
    private String searchCriteria;
    private String searchValue;
    private String suggestion;
}
