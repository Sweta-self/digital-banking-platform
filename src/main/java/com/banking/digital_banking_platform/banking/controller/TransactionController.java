package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.dto.FundTransferRequestDto;
import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;
import com.banking.digital_banking_platform.banking.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping("/transfer")
    public ResponseEntity<FundTransferResponseDto> transfer(
            @RequestHeader("Idempotency-Key")String idemKey,
            @RequestBody FundTransferRequestDto request){
return ResponseEntity.ok(transactionService.transferWithRetry(request,idemKey));
    }

}
