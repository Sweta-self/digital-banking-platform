package com.banking.digital_banking_platform.banking.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditRequestDto {
    private String accountNumber;
    private BigDecimal amount;
}
