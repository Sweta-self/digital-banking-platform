package com.banking.digital_banking_platform.banking.dto;

import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionStatementDto {

    private String referenceId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private TransactionStatus status;
    private LocalDateTime transactionDate;

}
