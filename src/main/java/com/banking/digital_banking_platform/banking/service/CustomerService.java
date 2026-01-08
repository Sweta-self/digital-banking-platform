package com.banking.digital_banking_platform.banking.service;

import com.banking.digital_banking_platform.banking.dto.CustomerRequestDto;

public interface CustomerService {
    Long createCustomer(CustomerRequestDto request);
}
