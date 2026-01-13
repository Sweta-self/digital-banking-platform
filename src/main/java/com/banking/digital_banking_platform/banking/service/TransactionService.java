package com.banking.digital_banking_platform.banking.service;

import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import com.banking.digital_banking_platform.banking.dto.FundTransferRequestDto;
import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;
import com.banking.digital_banking_platform.banking.dto.TransactionStatementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


public interface TransactionService {
    FundTransferResponseDto transferWithRetry(FundTransferRequestDto request,String idempotencyKey);
    Page<TransactionStatementDto>getStatement(
        String accountNumber,
        LocalDate from,
        LocalDate to,
        TransactionType type,
        Pageable pageable
     );
}
