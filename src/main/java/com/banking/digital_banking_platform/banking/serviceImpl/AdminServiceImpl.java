package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.KycStatus;
import com.banking.digital_banking_platform.banking.dto.KycUpdateRequestDto;
import com.banking.digital_banking_platform.banking.entity.Customer;
import com.banking.digital_banking_platform.banking.repository.CustomerRepository;
import com.banking.digital_banking_platform.banking.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CustomerRepository customerRepository;
    @Override
    @Transactional
    public void updateKycStatus(KycUpdateRequestDto request) {
        Customer customer=customerRepository.findById(request.getCustomerId())
                .orElseThrow(()-> new RuntimeException("Customer not found"));
        customer.setKycStatus(request.getKycStatus());
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> getPendingKycCustomers() {
       return customerRepository.findByKycStatus(KycStatus.PENDING);
    }
}
