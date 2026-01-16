package com.banking.digital_banking_platform.banking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    //TRANSACTION_INITIATED,TRANSFER_SUCCESS,APPROVE_TXN,REJECT_TXN

   private Long customerId; //NULL for system jobs

    private String actorType; //CUSTOMER /ADMIN /SYSTEM

    private String role;
    //USER ADMIN

    private String referenceId;

    private String status;

    private String message;

    private LocalDateTime createdAt;
}
