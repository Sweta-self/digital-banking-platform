package com.banking.digital_banking_platform.banking.recovery;

import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.entity.Account;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import com.banking.digital_banking_platform.banking.repository.AccountRepository;
import com.banking.digital_banking_platform.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LedgerRecoveryService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    @Transactional
    public void handleRecovery(Transaction debit, Transaction credit){
 try {
     //case 1:Debit done,credit pending->CREDIT Again
     if (debit.getStatus() == TransactionStatus.SUCCESS && credit.getStatus() ==
             TransactionStatus.INITIATED) {
         Account creditAcc =
                 accountRepository.findByAccountNumberForUpdate(
                         credit.getAccountNumber()
                 ).orElseThrow();
         creditAcc.setBalance(creditAcc.getBalance().add(credit.getAmount()));
         accountRepository.save(creditAcc);
         credit.setStatus(TransactionStatus.SUCCESS);
         transactionRepository.save(credit);

     }
     //case 2 Nothing happened->FAIL Both
     else if (debit.getStatus() == TransactionStatus.INITIATED &&
             credit.getStatus() == TransactionStatus.INITIATED) {
         debit.setStatus(TransactionStatus.FAILED);
         credit.setStatus(TransactionStatus.FAILED);

         transactionRepository.save(debit);
         transactionRepository.save(credit);
     }
     //case 3:Defensive safety(rare DB Crash case)
     else if (debit.getStatus() == TransactionStatus.INITIATED &&
             credit.getStatus() == TransactionStatus.SUCCESS) {
         Account debitAcc =
                 accountRepository.findByAccountNumberForUpdate(
                         debit.getAccountNumber()
                 ).orElseThrow();
         debitAcc.setBalance(
                 debitAcc.getBalance().subtract(debit.getAmount())
         );
         accountRepository.save(debitAcc);
         debit.setStatus(TransactionStatus.SUCCESS);
         transactionRepository.save(debit);
     }
     //case 4:Both SUCCESS-> NO-OP
 }catch(Exception e){
     //case 5 Exception during credit->mark creit failed and reversed debit(refund)
     credit.setStatus(TransactionStatus.FAILED);
     transactionRepository.save(credit);
     if(debit.getStatus()==TransactionStatus.SUCCESS){
         //Refund debit
         Account debitAcc=accountRepository.findByAccountNumberForUpdate(debit.getAccountNumber())
                 .orElseThrow();
         debitAcc.setBalance(debitAcc.getBalance().add(debit.getAmount()));
         accountRepository.save(debitAcc);
         //Mark debit as REFUNDED
         debit.setStatus(TransactionStatus.REVERSED);
         transactionRepository.save(debit);
     }
 }
    }
}
