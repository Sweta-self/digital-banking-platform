package com.banking.digital_banking_platform.banking.dto;

import com.banking.digital_banking_platform.banking.common.enums.AccountStatus;
import com.banking.digital_banking_platform.banking.common.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountResponseDto {
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal amount;
    private AccountStatus status;

}
