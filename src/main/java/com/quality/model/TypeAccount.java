package com.quality.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * TypeAccount entity representing account type catalog.
 * Stores different types of bank accounts (Savings, Checking, Credit, etc.)
 * Fields marked as nullable=false are also annotated with @NonNull for compile-time safety.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TypeAccount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idTypeAccount;
    
    @NonNull
    @Column(nullable = false, unique = true, length = 20)
    private String code;
    
    @NonNull
    @Column(nullable = false, length = 100)
    private String description;
    
    @NonNull
    @Column(nullable = false)
    private Boolean active = true;
}
