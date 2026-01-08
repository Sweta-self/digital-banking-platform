package com.banking.digital_banking_platform.banking.service;


import com.banking.digital_banking_platform.banking.dto.AccountRequestDto;
import com.banking.digital_banking_platform.banking.dto.AccountResponseDto;

public interface AccountService {
    AccountResponseDto openAccount(AccountRequestDto request);
}
