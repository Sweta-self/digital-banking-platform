package com.banking.digital_banking_platform.banking.service;

public interface AuditService {

    void log(String action,
             Long customerId,
             String actorType,
             String referenceId,
             String status,
             String message);
}
