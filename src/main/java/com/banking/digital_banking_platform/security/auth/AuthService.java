package com.banking.digital_banking_platform.security.auth;

import com.banking.digital_banking_platform.banking.entity.Customer;
import com.banking.digital_banking_platform.security.config.JwtUtil;
import com.banking.digital_banking_platform.security.user.Role;
import com.banking.digital_banking_platform.security.user.User;
import com.banking.digital_banking_platform.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //Register
    public void register(RegisterRequest request){

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already registered");
        }

        User user=new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

    }

    //Login
    public LoginResponse login(LoginRequest request){
        User user =userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("Invalid email"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }
        String token=jwtUtil.generateToken( user.getEmail(),user.getRole());
        return new LoginResponse(token);
    }
}
