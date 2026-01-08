package com.banking.digital_banking_platform.banking.common.util;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {
public String generateAccountNumber(){
    return "DB"+System.currentTimeMillis();
}
}
