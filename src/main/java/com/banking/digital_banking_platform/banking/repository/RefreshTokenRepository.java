package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.RefreshToken;
import com.banking.digital_banking_platform.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
