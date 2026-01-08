package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.dto.AccountRequestDto;
import com.banking.digital_banking_platform.banking.dto.AccountResponseDto;
import com.banking.digital_banking_platform.banking.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
