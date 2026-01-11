package com.banking.digital_banking_platform.banking.serviceImpl;

import com.banking.digital_banking_platform.banking.dto.FundTransferResponseDto;
import com.banking.digital_banking_platform.banking.entity.IdempotencyRecord;
import com.banking.digital_banking_platform.banking.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void saveIdempotency(
            String key,
            FundTransferResponseDto response){
        IdempotencyRecord record=new IdempotencyRecord();
        record.setIdempotencyKey(key);
        record.setTransactionId(response.getTransactionId());
        record.setResponseMessage(response.getMessage());
        record.setCreatedAt(LocalDateTime.now());
        idempotencyRepository.save(record);
    }

    @Transactional(readOnly = true)
    public Optional<IdempotencyRecord> find(String key){
        return idempotencyRepository.findByIdempotencyKey(key);
    }
}
