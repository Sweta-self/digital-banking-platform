package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.RefreshToken;
import com.banking.digital_banking_platform.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken>findByUserId(Long userId);
    void deleteByUser(User user);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked=true WHERE r.user.id=:userId")
    void revokeAllByUser(@Param("userId") Long userId);
}
