package com.banking.digital_banking_platform.banking.repository;

import com.banking.digital_banking_platform.banking.entity.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<
        IdempotencyRecord,Long> {
    Optional<IdempotencyRecord>findByIdempotencyKey(String key);

}
