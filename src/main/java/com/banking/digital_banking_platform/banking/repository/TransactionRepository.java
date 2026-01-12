package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction>findByStatusAndTransactionType(
            TransactionStatus status,
            TransactionType transactionType
    );
    List<Transaction>findByReferenceId(String refernceId);
}

