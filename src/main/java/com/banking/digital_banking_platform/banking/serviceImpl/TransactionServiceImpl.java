package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.AccountStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import com.banking.digital_banking_platform.banking.dto.FundTransferRequestDto;
import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;
import com.banking.digital_banking_platform.banking.entity.Account;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import com.banking.digital_banking_platform.banking.repository.AccountRepository;
import com.banking.digital_banking_platform.banking.repository.TransactionRepository;
import com.banking.digital_banking_platform.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.hibernate.PessimisticLockException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransferInternalService transferInternalService;
     private static final int MAX_RETRY=3;
    @Override
    public FundTransferResponseDto transferWithRetry(FundTransferRequestDto request) {
    int attempt=0;
    while(attempt<MAX_RETRY){
        try{
            return transferInternalService.transferFunds(request);
        }
        catch(Exception e){
            attempt++;
            if(attempt == MAX_RETRY){
                throw e;
            }
            try{
                Thread.sleep(200);
            }
            catch(InterruptedException ignored){}
        }
    }
  throw new RuntimeException("Transfer failed retries");
    }




}
