package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.AccountStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.dto.FundTransferRequestDto;
import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;
import com.banking.digital_banking_platform.banking.entity.Account;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import com.banking.digital_banking_platform.banking.repository.AccountRepository;
import com.banking.digital_banking_platform.banking.repository.TransactionRepository;
import lombok.AllArgsConstructor;
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
public class TransferInternalService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public FundTransferResponseDto transferFunds(FundTransferRequestDto request) {
        try{
            //Always lock in SAME ORDER to reduce deadlock
            Account debitAcc=lockAccount(
                    request.getSenderAccount(),
                    request.getReceiverAccount(),
                    true);
            Account creditAcc=lockAccount(
                    request.getSenderAccount(),
                    request.getReceiverAccount(),
                    false);
            validateAccount(debitAcc);
            validateAccount(creditAcc);
            if(debitAcc.getBalance().compareTo(request.getAmount())<0){
                throw new RuntimeException("Insufficient Balance");
            }
            //Debit
            debitAcc.setBalance(debitAcc.getBalance().subtract(request.getAmount()));
            //Credit
            creditAcc.setBalance(creditAcc.getBalance().add(request.getAmount()));

            accountRepository.save(debitAcc);
            accountRepository.save(creditAcc);

            Transaction tx= saveTransaction(
                    request.getSenderAccount(),
                    request.getReceiverAccount(),
                    request.getAmount());
            return new FundTransferResponseDto(
                    "Transfer successful",
                    tx.getReferenceId()
            );
        }
        catch(PessimisticLockException | CannotAcquireLockException e){
            throw new RuntimeException("Deadlock occured",e);
        }

    }
    private Account lockAccount(String from,String to,boolean isDebit){
        String accNo=isDebit
                ?(from.compareTo(to)<0?from:to)
                :(from.compareTo(to)<0?to:from);
        return accountRepository.findByAccountNumberForUpdate(accNo)
                .orElseThrow(()->new RuntimeException("Account not found"));

    }
    private void validateAccount(Account acc){
        if(acc.getStatus()!= AccountStatus.ACTIVE){
            throw new RuntimeException("Account is Blocked");
        }
    }
    private Transaction saveTransaction(String from, String to, BigDecimal amount)
    {
        Transaction txn=new Transaction();
        txn.setSenderAccount(from);
        txn.setReceiverAccount(to);
        txn.setReferenceId(UUID.randomUUID().toString());
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setAmount(amount);
        txn.setTransactionDate(LocalDateTime.now());
       return  transactionRepository.save(txn);

    }
}
