package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.common.enums.KycStatus;
import com.banking.digital_banking_platform.banking.dto.CustomerRequestDto;
import com.banking.digital_banking_platform.banking.entity.Customer;
import com.banking.digital_banking_platform.banking.repository.CustomerRepository;
import com.banking.digital_banking_platform.banking.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Long createCustomer(CustomerRequestDto request) {
       if(customerRepository.existsByEmail(request.getEmail())){
           throw new RuntimeException("Customer already exists");
       }
       Customer customer=new Customer();
       customer.setFullName(request.getFullName());
       customer.setEmail(request.getEmail());
       customer.setMobile(request.getMobile());
       customer.setKycStatus(KycStatus.PENDING);

       return customerRepository.save(customer).getId();

    }
}
