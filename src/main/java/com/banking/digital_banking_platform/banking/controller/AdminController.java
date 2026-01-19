package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.dto.AdminCreateRequest;
import com.banking.digital_banking_platform.banking.dto.KycUpdateRequestDto;
import com.banking.digital_banking_platform.banking.entity.Customer;
import com.banking.digital_banking_platform.banking.service.AdminService;
import com.banking.digital_banking_platform.security.user.Role;
import com.banking.digital_banking_platform.security.user.User;
import com.banking.digital_banking_platform.security.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @PostMapping("/createAdmin")
    public ResponseEntity<String> createAdmin(
            @RequestBody AdminCreateRequest request
    ){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Admin already exists");
        }
        User admin=new User();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        return ResponseEntity
                .ok("Admin create successfully");

    }
}
