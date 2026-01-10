package com.banking.digital_banking_platform.banking.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditResponseDto {
    private String status;
    private String message;
    private String accountNumber;
    private BigDecimal amount;
    private BigDecimal currentBalance;
}
