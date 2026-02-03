package com.quality.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * Currency entity representing currency catalog.
 * Stores different currencies (USD, PEN, EUR, etc.) with their symbols.
 * Fields marked as nullable=false are also annotated with @NonNull for compile-time safety.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Currency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idCurrency;
    
    @NonNull
    @Column(nullable = false, unique = true, length = 3)
    private String code;
    
    @NonNull
    @Column(nullable = false, length = 50)
    private String name;
    
    @NonNull
    @Column(nullable = false, length = 5)
    private String symbol;
    
    @NonNull
    @Column(nullable = false)
    private Boolean active = true;
}
