package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.*;
import com.banking.digital_banking_platform.banking.common.util.NotificationTemplateBuilder;
import com.banking.digital_banking_platform.banking.dto.FundTransferRequestDto;
import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;
import com.banking.digital_banking_platform.banking.dto.NotificationData;
import com.banking.digital_banking_platform.banking.entity.Account;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import com.banking.digital_banking_platform.banking.repository.AccountRepository;
import com.banking.digital_banking_platform.banking.repository.TransactionRepository;
import com.banking.digital_banking_platform.banking.service.NotificationService;
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
    private final NotificationService notificationService;
    private final NotificationTemplateBuilder templateBuilder;

    @Transactional
    public FundTransferResponseDto transferFunds(FundTransferRequestDto request,String refId ) {

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
            //Save debit Ledger
            Transaction debitTx= createLedger(
                    refId,
                    request.getSenderAccount(),
                    TransactionType.DEBIT,
                    request.getAmount());
            //save credit ledger
            Transaction creditTx= createLedger(
                    refId,
                    request.getReceiverAccount(),
                    TransactionType.CREDIT,
                    request.getAmount());


            //Debit
            debitAcc.setBalance(debitAcc.getBalance().subtract(request.getAmount()));
            //Credit
            creditAcc.setBalance(creditAcc.getBalance().add(request.getAmount()));

            accountRepository.save(debitAcc);
            accountRepository.save(creditAcc);

           //MARK SUCCESS
            debitTx.setStatus(TransactionStatus.SUCCESS);
            creditTx.setStatus(TransactionStatus.SUCCESS);

            transactionRepository.save(debitTx);
            transactionRepository.save(creditTx);

            NotificationData data= new NotificationData();
            data.setAmount(request.getAmount());
            data.setToAccount(creditAcc.getAccountNumber());

            String message= templateBuilder.buildMessage(
                    NotificationEvent.FUND_TRANSFER_SUCCESS,
                    data
            );
            notificationService.sendNotification(
                    request.getSenderAccount(),
                    NotificationType.PUSH,
                    message,refId
            );
            return new FundTransferResponseDto(
                    "Transfer successful",
                    refId
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
    private Transaction createLedger(
            String refId,
            String accNo,
            TransactionType type,
            BigDecimal amount
    ){
        Transaction txn=new Transaction();
        txn.setReferenceId(refId);
        txn.setAccountNumber(accNo);
        txn.setTransactionType(type);
        txn.setAmount(amount);
        txn.setStatus(TransactionStatus.INITIATED);
        txn.setTransactionDate(LocalDateTime.now());
        return  transactionRepository.save(txn);
    }

}
