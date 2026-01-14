package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.common.enums.NotificationStatus;
import com.banking.digital_banking_platform.banking.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification>findByStatus(NotificationStatus status);
}
