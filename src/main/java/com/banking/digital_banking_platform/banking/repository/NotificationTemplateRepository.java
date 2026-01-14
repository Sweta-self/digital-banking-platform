package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateRepository extends JpaRepository<
        NotificationTemplate,Long> {
    Optional<NotificationTemplate>findByCodeAndActiveTrue(String code);
}
