package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.dto.AccountRequestDto;
import com.banking.digital_banking_platform.banking.dto.AccountResponseDto;
import com.banking.digital_banking_platform.banking.dto.CreditRequestDto;
import com.banking.digital_banking_platform.banking.dto.CreditResponseDto;
import com.banking.digital_banking_platform.banking.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/openaccount")
    public ResponseEntity<AccountResponseDto>openAccount(
            @Valid @RequestBody AccountRequestDto request
            ){
        return ResponseEntity.ok(accountService.openAccount(request));
    }
    @PostMapping("/credit")
    public ResponseEntity<CreditResponseDto>creditAmount(
            @RequestBody CreditRequestDto request
            ){
        CreditResponseDto response=accountService.credit(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/debit")
    public ResponseEntity<CreditResponseDto>debitAmount(
            @RequestBody CreditRequestDto request
    ){
        CreditResponseDto response=accountService.debit(request);
        return ResponseEntity.ok(response);
    }

}
