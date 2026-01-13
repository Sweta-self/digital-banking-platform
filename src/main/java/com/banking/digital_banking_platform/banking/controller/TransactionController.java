package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.common.enums.TransactionType;
import com.banking.digital_banking_platform.banking.dto.FundTransferRequestDto;
import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;
import com.banking.digital_banking_platform.banking.dto.TransactionStatementDto;
import com.banking.digital_banking_platform.banking.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @GetMapping("/{accountNumber}/getStatement")
public Page<TransactionStatementDto>getStatement(
            @PathVariable String accountNumber,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam(required = false)TransactionType type,
            @PageableDefault(size=10,sort="transactionDate",
             direction= Sort.Direction.DESC) Pageable pageable){

        return transactionService.getStatement(
                accountNumber, fromDate, toDate, type, pageable);
    }
}
