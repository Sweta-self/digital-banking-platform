package com.banking.digital_banking_platform.banking.service;

import com.banking.digital_banking_platform.banking.dto.KycUpdateRequestDto;
import com.banking.digital_banking_platform.banking.entity.Customer;

import java.util.List;

public interface AdminService {
   void updateKycStatus(KycUpdateRequestDto request);
   List<Customer> getPendingKycCustomers();
}
