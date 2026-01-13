package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction>findByStatusAndTransactionType(
            TransactionStatus status,
            TransactionType transactionType
    );
    List<Transaction>findByReferenceId(String refernceId);
    Page<Transaction>findByAccountNumberAndTransactionDateBetween(
            String accountNumber,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );
    Page<Transaction>findByAccountNumberAndTransactionTypeAndTransactionDateBetween(
            String accountNumber,
            TransactionType type,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );
}

