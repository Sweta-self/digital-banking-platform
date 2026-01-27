package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice> findByUserIdAndDeviceId(
            Long userId,
            String deviceId
    );
    List<UserDevice> findByUserIdAndActiveTrue(Long userId);
    long countByUserIdAndActiveTrue(Long userId);
    List<UserDevice>findByUserIdAndActiveTrueOrderByLastLoginAsc(Long userId);
    List<UserDevice>findByUserId(Long userId);
    Optional<UserDevice>findTopByUserIdOrderByLastLoginDesc(Long userId);
}
