package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.AccountStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import com.banking.digital_banking_platform.banking.dto.FundTransferRequestDto;
import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;
import com.banking.digital_banking_platform.banking.dto.TransactionStatementDto;
import com.banking.digital_banking_platform.banking.entity.Account;
import com.banking.digital_banking_platform.banking.entity.IdempotencyRecord;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import com.banking.digital_banking_platform.banking.repository.AccountRepository;
import com.banking.digital_banking_platform.banking.repository.IdempotencyRepository;
import com.banking.digital_banking_platform.banking.repository.TransactionRepository;
import com.banking.digital_banking_platform.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.hibernate.PessimisticLockException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


   private final TransactionRepository transactionRepository;
    private final TransferInternalService transferInternalService;
    private final IdempotencyRepository idempotencyRepository;
    private final IdempotencyService idempotencyService;

     private static final int MAX_RETRY=3;
    @Override
    public FundTransferResponseDto transferWithRetry(FundTransferRequestDto request,
    String idempotencyKey) {
        //Check idempotency
        Optional<IdempotencyRecord>existing=idempotencyRepository
                .findByIdempotencyKey(idempotencyKey);
        if(existing.isPresent()){
            return new FundTransferResponseDto(
                    existing.get().getResponseMessage(),
            existing.get().getTransactionId());
        }
    int attempt=0;
    while(attempt<MAX_RETRY){
        try{
            FundTransferResponseDto response=
                    transferInternalService.transferFunds(request,idempotencyKey);
            idempotencyService.saveIdempotency(idempotencyKey,response);
            return response;
        }
        catch(PessimisticLockException | CannotAcquireLockException  e){
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

    @Override
    public Page<TransactionStatementDto> getStatement(String accountNumber, LocalDate from, LocalDate to, TransactionType type, Pageable pageable) {

        LocalDateTime fromDate=from.atStartOfDay();
        LocalDateTime toDate=to.atTime(23,59,59);

        Page<Transaction>page;
        if(type!=null){
            page=transactionRepository
                    .findByAccountNumberAndTransactionTypeAndTransactionDateBetween(
                            accountNumber,type,fromDate,toDate,pageable
                    );
        }
        else{
            page=transactionRepository.findByAccountNumberAndTransactionDateBetween(
                    accountNumber,
                    fromDate,
                    toDate,pageable
            );
        }
        return  page.map(tx->new TransactionStatementDto(
                tx.getReferenceId(),
                tx.getTransactionType(),
                tx.getAmount(),
                tx.getStatus(),
                tx.getTransactionDate()
        ));
    }


}
