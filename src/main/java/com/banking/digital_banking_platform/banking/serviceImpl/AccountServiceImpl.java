package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.AccountStatus;
import com.banking.digital_banking_platform.banking.common.enums.KycStatus;
import com.banking.digital_banking_platform.banking.common.util.AccountNumberGenerator;
import com.banking.digital_banking_platform.banking.dto.AccountRequestDto;
import com.banking.digital_banking_platform.banking.dto.AccountResponseDto;
import com.banking.digital_banking_platform.banking.entity.Account;
import com.banking.digital_banking_platform.banking.entity.Customer;
import com.banking.digital_banking_platform.banking.repository.AccountRepository;
import com.banking.digital_banking_platform.banking.repository.CustomerRepository;
import com.banking.digital_banking_platform.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    @Override
    public AccountResponseDto openAccount(AccountRequestDto request) {
        Customer customer= customerRepository.findById(request.getCustomerId())
                .orElseThrow(()->new RuntimeException("customer not found"));

        if(!customer.getKycStatus().equals(KycStatus.VERIFIED)){
            throw new RuntimeException(("KYC NOT VERIFIED"));
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
}
