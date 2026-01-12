package com.banking.digital_banking_platform.banking.recovery;

import com.banking.digital_banking_platform.banking.common.enums.TransactionStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import com.banking.digital_banking_platform.banking.repository.AccountRepository;
import com.banking.digital_banking_platform.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LedgerRecoveryScheduler {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
  private final LedgerRecoveryService ledgerRecoveryService;

    @Scheduled(fixedDelay=60000)
    public void recoverLedgerTransactions() {
        //step 1 take initiated entry only
       List<Transaction>initiated=
               transactionRepository.findByStatus(TransactionStatus.INITIATED);

       //step 2 referencedId wise group
        Map<String,List<Transaction>> grouped=
                initiated.stream()
                        .collect(Collectors.groupingBy(Transaction::getReferenceId));

        for(Map.Entry<String,List<Transaction>>entry:grouped.entrySet()){
            String refId= entry.getKey();
          List<Transaction>txns=  transactionRepository.findByReferenceId(refId);

          Transaction debit=txns.stream()
                  .filter(t->t.getTransactionType()==
                          TransactionType.DEBIT
                          )
                  .findFirst().orElse(null);

          Transaction credit=txns.stream()
                          .filter(t->t.getTransactionType()==
                                  TransactionType.CREDIT)
                                  .findFirst().orElse(null);
          if(debit !=null && credit != null) {
              {
                  ledgerRecoveryService.handleRecovery(debit, credit);
              }
          }
        }


    }
}
