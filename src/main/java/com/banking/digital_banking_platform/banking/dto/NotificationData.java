package com.banking.digital_banking_platform.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationData {
    private BigDecimal amount;
    private String toAccount;
    private String fromAccount;

}
