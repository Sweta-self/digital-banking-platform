package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
}
