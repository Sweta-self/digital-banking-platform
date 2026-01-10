package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.AccountStatus;
import com.banking.digital_banking_platform.banking.common.enums.KycStatus;
import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import com.banking.digital_banking_platform.banking.common.util.AccountNumberGenerator;
import com.banking.digital_banking_platform.banking.dto.AccountRequestDto;
import com.banking.digital_banking_platform.banking.dto.AccountResponseDto;
import com.banking.digital_banking_platform.banking.dto.CreditRequestDto;
import com.banking.digital_banking_platform.banking.dto.CreditResponseDto;
import com.banking.digital_banking_platform.banking.entity.Account;
import com.banking.digital_banking_platform.banking.entity.Customer;
import com.banking.digital_banking_platform.banking.entity.Transaction;
import com.banking.digital_banking_platform.banking.repository.AccountRepository;
import com.banking.digital_banking_platform.banking.repository.CustomerRepository;
import com.banking.digital_banking_platform.banking.repository.TransactionRepository;
import com.banking.digital_banking_platform.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public AccountResponseDto openAccount(AccountRequestDto request) {
        Customer customer= customerRepository.findById(request.getCustomerId())
                .orElseThrow(()->new RuntimeException("customer not found"));
 KycStatus status=customer.getKycStatus();
 if(status==KycStatus.PENDING){
     throw new RuntimeException("KYC is pending verification");
 }
 if(status==KycStatus.REJECTED){
     throw new RuntimeException("KYC rejected.Please re-submit documents");
 }

        Account account=new Account();
        account.setAccountNumber(accountNumberGenerator.generateAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setCustomer(customer);
        Account savedAccount=accountRepository.save(account);

        return new AccountResponseDto(
                savedAccount.getAccountNumber(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getStatus()

        );
    }

    @Override
    @Transactional
    public CreditResponseDto credit(CreditRequestDto requestDto) {
        Account account=accountRepository.findByAccountNumberForUpdate(requestDto.getAccountNumber())
                .orElseThrow(()->new RuntimeException("Account not found"));
        //increase Balance
        BigDecimal newBalance=account.getBalance().add(requestDto.getAmount());
        account.setBalance(newBalance);

        //create CREDIT transaction
        Transaction txn=new Transaction();
        txn.setAccount(account);
        txn.setAmount(requestDto.getAmount());
        txn.setTransactionType(TransactionType.CREDIT);
        txn.setReferenceId(UUID.randomUUID().toString());
        txn.setClosingBalance(newBalance);
        txn.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(txn);
        accountRepository.save(account);
        CreditResponseDto response=new CreditResponseDto();
        response.setStatus("SUCCESS");
        response.setMessage("Amount credited successfully");
        response.setAccountNumber(account.getAccountNumber());
        response.setAmount(requestDto.getAmount());
        response.setCurrentBalance(newBalance);
        return response;


    }

    @Override
    @Transactional
    public CreditResponseDto debit(CreditRequestDto request) {
       Account account=accountRepository.findByAccountNumberForUpdate(request.getAccountNumber())
               .orElseThrow(()->new RuntimeException("Account not found"));

       if(account.getBalance().compareTo(request.getAmount())<0){
           throw  new RuntimeException("Insufficient Balance");
       }
       BigDecimal newBalance=account.getBalance().subtract(request.getAmount());
       account.setBalance(newBalance);

        Transaction txn=new Transaction();
        txn.setAccount(account);
        txn.setTransactionType(TransactionType.DEBIT);
        txn.setAmount(request.getAmount());
        txn.setClosingBalance(newBalance);
        txn.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(txn);
        accountRepository.save(account);

        CreditResponseDto response=new CreditResponseDto();
        response.setStatus("SUCCESS");
        response.setMessage("Amount debited successfully");
        response.setAccountNumber(account.getAccountNumber());
        response.setAmount(request.getAmount());
        response.setCurrentBalance(newBalance);
        return response;

    }
}
