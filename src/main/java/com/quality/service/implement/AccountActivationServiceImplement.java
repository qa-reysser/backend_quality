package com.quality.service.implement;

import com.quality.dto.AccountActivationDTO;
import com.quality.model.*;
import com.quality.repository.IAccountActivationRepository;
import com.quality.repository.IGenericRepository;
import com.quality.service.IAccountActivationService;
import com.quality.service.IAccountService;
import com.quality.service.ITypeDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Implementation of IAccountActivationService.
 * Handles account activation with document verification.
 * Validates that the provided document information matches the account owner.
 * Records all activation attempts (successful and failed) for audit trail.
 * Follows Dependency Inversion Principle (DIP) and Single Responsibility Principle (SRP).
 */
@Service
@RequiredArgsConstructor
public class AccountActivationServiceImplement extends OperationsImplement<AccountActivation, Integer> implements IAccountActivationService {
    
    private final IAccountActivationRepository repo;
    private final IAccountService accountService;
    private final ITypeDocumentService typeDocumentService;
    
    @Override
    @NonNull
    protected IGenericRepository<AccountActivation, Integer> getRepo() {
        return repo;
    }
    
    @Override
    @NonNull
    protected String getResourceType() {
        return "AccountActivation";
    }
    
    @Override
    @Transactional
    @NonNull
    public AccountActivation activateAccount(@NonNull AccountActivationDTO dto) {
        Objects.requireNonNull(dto, "AccountActivationDTO cannot be null");
        Objects.requireNonNull(dto.getAccountNumberProvided(), "Account number cannot be null");
        Objects.requireNonNull(dto.getDocumentNumberProvided(), "Document number cannot be null");
        Objects.requireNonNull(dto.getIdTypeDocumentProvided(), "Type document ID cannot be null");
        
        // Find account by account number
        Account account = accountService.findByAccountNumber(dto.getAccountNumberProvided());
        
        // Get the type document provided
        TypeDocument typeDocumentProvided = typeDocumentService.findById(dto.getIdTypeDocumentProvided());
        
        // Get the account owner's information
        Client accountOwner = account.getClient();
        
        // Validate the provided information against the account owner
        boolean isValid = validateActivation(
                accountOwner,
                typeDocumentProvided,
                dto.getDocumentNumberProvided()
        );
        
        // Create activation record
        AccountActivation activation = new AccountActivation();
        activation.setAccount(account);
        activation.setTypeDocumentProvided(typeDocumentProvided);
        activation.setDocumentNumberProvided(dto.getDocumentNumberProvided());
        activation.setAccountNumberProvided(dto.getAccountNumberProvided());
        activation.setAttemptDate(LocalDateTime.now());
        
        if (isValid) {
            // Validation successful - activate account
            activation.setActivationStatus(ActivationStatus.SUCCESS);
            activation.setErrorReason(null);
            
            // Update account status to ACTIVE
            account.setStatus(AccountStatus.ACTIVE);
            account.setActivatedDate(LocalDateTime.now());
            accountService.update(account, account.getIdAccount());
        } else {
            // Validation failed - record the failure
            activation.setActivationStatus(ActivationStatus.FAILED);
            activation.setErrorReason(buildErrorReason(accountOwner, typeDocumentProvided, dto.getDocumentNumberProvided()));
        }
        
        // Save and return the activation record
        return repo.save(activation);
    }
    
    /**
     * Validates that the provided document information matches the account owner.
     * Follows Single Responsibility Principle (SRP) - encapsulates validation logic.
     * 
     * @param accountOwner the client who owns the account
     * @param typeDocumentProvided the type of document provided
     * @param documentNumberProvided the document number provided
     * @return true if validation passes, false otherwise
     */
    private boolean validateActivation(
            @NonNull Client accountOwner,
            @NonNull TypeDocument typeDocumentProvided,
            @NonNull String documentNumberProvided
    ) {
        // Check if type document matches
        boolean typeDocumentMatches = accountOwner.getTypeDocument().getIdTypeDocument()
                .equals(typeDocumentProvided.getIdTypeDocument());
        
        // Check if document number matches
        boolean documentNumberMatches = accountOwner.getDocumentNumber()
                .equals(documentNumberProvided);
        
        return typeDocumentMatches && documentNumberMatches;
    }
    
    /**
     * Builds a descriptive error reason when validation fails.
     * Follows Open/Closed Principle (OCP) - easy to extend with more detailed reasons.
     * 
     * @param accountOwner the client who owns the account
     * @param typeDocumentProvided the type of document provided
     * @param documentNumberProvided the document number provided
     * @return a descriptive error message
     */
    private String buildErrorReason(
            @NonNull Client accountOwner,
            @NonNull TypeDocument typeDocumentProvided,
            @NonNull String documentNumberProvided
    ) {
        boolean typeDocumentMatches = accountOwner.getTypeDocument().getIdTypeDocument()
                .equals(typeDocumentProvided.getIdTypeDocument());
        boolean documentNumberMatches = accountOwner.getDocumentNumber()
                .equals(documentNumberProvided);
        
        if (!typeDocumentMatches && !documentNumberMatches) {
            return "Type document and document number do not match account owner";
        } else if (!typeDocumentMatches) {
            return "Type document does not match account owner";
        } else {
            return "Document number does not match account owner";
        }
    }
}
