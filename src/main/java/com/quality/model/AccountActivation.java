package com.quality.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

/**
 * AccountActivation entity representing account activation attempts.
 * Records all activation attempts (successful and failed) for audit trail.
 * Validates that the provided document information matches the account owner.
 * Fields marked as nullable=false are also annotated with @NonNull for compile-time safety.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountActivation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idAccountActivation;
    
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_account", nullable = false)
    private Account account;
    
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_document_provided", nullable = false)
    private TypeDocument typeDocumentProvided;
    
    @NonNull
    @Column(nullable = false, length = 20)
    private String documentNumberProvided;
    
    @NonNull
    @Column(nullable = false, length = 20)
    private String accountNumberProvided;
    
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivationStatus activationStatus;
    
    @Column(length = 255)
    private String errorReason;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime attemptDate = LocalDateTime.now();
    
    @PrePersist
    protected void onCreate() {
        attemptDate = LocalDateTime.now();
    }
}
