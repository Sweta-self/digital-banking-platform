package com.banking.digital_banking_platform.banking.controller;

import com.banking.digital_banking_platform.banking.dto.CustomerRequestDto;
import com.banking.digital_banking_platform.banking.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/createcustomer")
    public ResponseEntity<Long> createCustomer(@Valid
                                              @RequestBody CustomerRequestDto request){
        return ResponseEntity.ok(customerService.createCustomer(request)) ;
    }
}
