package com.banking.digital_banking_platform.banking.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FundTransferRequestDto {
    private String senderAccount;
    private String receiverAccount;
    private BigDecimal amount;
}
