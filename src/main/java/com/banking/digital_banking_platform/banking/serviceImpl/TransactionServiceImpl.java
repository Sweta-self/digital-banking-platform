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

    @Override
    @Transactional
    public FundTransferResponseDto transferFunds(FundTransferRequestDto request) {
        String refId=UUID.randomUUID().toString();
        try {
            Account sender = accountRepository.findByAccountNumber(request.getSenderAccount())
                    .orElseThrow(() -> new RuntimeException("Sender account not found"));

            Account receiver = accountRepository.findByAccountNumber(request.getReceiverAccount())
                    .orElseThrow(() -> new RuntimeException("Receiver account not found"));

            // Status checks
            if (sender.getStatus() != AccountStatus.ACTIVE) {
                throw new RuntimeException("Sender account not eligible for Transfer");
            }
            if (receiver.getStatus() != AccountStatus.ACTIVE) {
                throw new RuntimeException("Receiver account inactive");
            }
            if (sender.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient Balance");
            }
            //Debit and credit
            sender.setBalance(sender.getBalance().subtract(request.getAmount()));
            receiver.setBalance(receiver.getBalance().add(request.getAmount()));

            accountRepository.save(sender);
            accountRepository.save(receiver);

            //save Transaction


            transactionRepository.save(buildTxn(
                    sender.getAccountNumber(),
                    refId,
                    request.getAmount(),
                    TransactionType.DEBIT
            ));
            transactionRepository.save(buildTxn(
                    receiver.getAccountNumber(),
                    refId,
                    request.getAmount(),
                    TransactionType.CREDIT
            ));
            return new FundTransferResponseDto(
                    "Transaction successful",
                    refId
            );
        }
        catch(Exception ex){


            throw ex;//rollback
        }

    }
    private Transaction buildTxn(String accNo, String refId,
                                 BigDecimal amount,
                                 TransactionType type){
        Transaction txn=new Transaction();
        txn.setReferenceId(refId);
        txn.setTransactionType(type);
        txn.setAccountNumber(accNo);
        txn.setStatus(TransactionStatus.SUCCESS);
        txn.setAmount(amount);
        txn.setTransactionDate(LocalDateTime.now());
        return txn;
    }
}
