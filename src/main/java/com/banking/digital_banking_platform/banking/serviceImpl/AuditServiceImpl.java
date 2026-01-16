package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.entity.AuditLog;
import com.banking.digital_banking_platform.banking.repository.AuditLogRepository;
import com.banking.digital_banking_platform.banking.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String action, Long customerId, String actorType, String referenceId, String status, String message) {
        AuditLog log= new AuditLog();
        log.setAction(action);
        log.setCustomerId(customerId);
        log.setActorType(actorType);
        log.setReferenceId(referenceId);
        log.setStatus(status);
        log.setMessage(message);
        log.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(log);
    }


}
