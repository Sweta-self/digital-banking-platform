package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByEmail(String email);

}
