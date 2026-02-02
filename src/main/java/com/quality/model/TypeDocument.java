package com.quality.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * TypeDocument entity representing document type catalog.
 * Stores different types of identification documents (DNI, Passport, RUC, etc.)
 * with validation rules and constraints.
 * Fields marked as nullable=false are also annotated with @NonNull for compile-time safety.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TypeDocument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idTypeDocument;
    
    @NonNull
    @Column(nullable = false, unique = true, length = 20)
    private String code;
    
    @NonNull
    @Column(nullable = false, length = 100)
    private String description;
    
    @Column(length = 100)
    private String validationPattern;
    
    private Integer minLength;
    
    private Integer maxLength;
    
    @NonNull
    @Column(nullable = false)
    private Boolean active = true;
}
