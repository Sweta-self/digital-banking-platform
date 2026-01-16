package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.UserFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserFcmTokenRepository extends JpaRepository<
        UserFcmToken,Long> {
    Optional<UserFcmToken>findByUserIdAndFcmToken(Long userId,String fcmToken);


    @Query("select u.fcmToken from UserFcmToken u where u.userId=:userId")
    List<String> findTokenByUserId(Long userId);
}
