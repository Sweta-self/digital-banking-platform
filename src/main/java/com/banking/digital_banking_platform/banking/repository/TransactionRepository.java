package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
