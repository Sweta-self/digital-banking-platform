package com.banking.digital_banking_platform.banking.bootstrap;

import com.banking.digital_banking_platform.security.user.Role;
import com.banking.digital_banking_platform.security.user.User;
import com.banking.digital_banking_platform.security.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrap {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createInitialAdmin(){
        if(!userRepository.existsByEmail("admin@bank.com")){
            User admin=new User();
            admin.setEmail("admin@bank.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }
}
