package com.quality.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * Client entity representing customer information.
 * Stores personal data and identification details for clients.
 * Fields marked as nullable=false are also annotated with @NonNull for compile-time safety.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idClient;
    
    @NonNull
    @Column(nullable = false, length = 50)
    private String firstName;
    
    @NonNull
    @Column(nullable = false, length = 50)
    private String lastName;
    
    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_document", nullable = false)
    private TypeDocument typeDocument;
    
    @NonNull
    @Column(nullable = false, unique = true, length = 20)
    private String documentNumber;
    
    @NonNull
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @NonNull
    @Column(nullable = false, length = 20)
    private String phone;
}
