package com.banking.digital_banking_platform.banking.service;

import com.banking.digital_banking_platform.banking.dto.FundTransferRequestDto;
import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;

public interface TransactionService {
    FundTransferResponseDto transferWithRetry(FundTransferRequestDto request,String idempotencyKey);
}
