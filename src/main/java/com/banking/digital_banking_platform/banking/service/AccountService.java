package com.banking.digital_banking_platform.banking.service;


import com.banking.digital_banking_platform.banking.dto.AccountRequestDto;
import com.banking.digital_banking_platform.banking.dto.AccountResponseDto;
import com.banking.digital_banking_platform.banking.dto.CreditRequestDto;
import com.banking.digital_banking_platform.banking.dto.CreditResponseDto;

import java.math.BigDecimal;

public interface AccountService {
    AccountResponseDto openAccount(AccountRequestDto request);
    CreditResponseDto credit(CreditRequestDto request);
    CreditResponseDto debit(CreditRequestDto request);
}
