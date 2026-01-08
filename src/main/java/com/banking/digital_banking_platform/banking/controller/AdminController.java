package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.dto.KycUpdateRequestDto;
import com.banking.digital_banking_platform.banking.entity.Customer;
import com.banking.digital_banking_platform.banking.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/kyc/update")
    public ResponseEntity<String>updateKyc(
            @Valid
            @RequestBody KycUpdateRequestDto request
            ){
        adminService.updateKycStatus(request);
        return ResponseEntity.ok("KYC status updated Successfully");
    }

    @GetMapping("/kyc/pending")
    public ResponseEntity<List<Customer>>getPendingKyc(){
        return ResponseEntity.ok(adminService.getPendingKycCustomers());
    }
}
